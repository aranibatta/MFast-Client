#undef UNICODE

#include "DCAppConnector.h"
#include "configParams.h"
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>
#include <process.h>
#include <direct.h>

#include <string>
#include <algorithm>
#include <vector>

// Need to link with Ws2_32.lib
#pragma comment (lib, "Ws2_32.lib")
#define DEFAULT_PORT "9015"

#define DEBUG_OUTPUT 0

char *dcExeName;
extern ConfigFile *configFile;

char *changeToWorkingDirAndGetExeName(char *fullExePath){
	char* exePath = (char *)malloc(strlen(fullExePath) + 1);
	memset(exePath, 0, strlen(fullExePath) + 1);
	memcpy(exePath, fullExePath, strlen(fullExePath));
	char* exeName = strrchr(exePath, '\\');
	if(exeName == 0){ // we're already in the directory or the directory wasn't passed in as part of the exe name
		return exePath;
	}
	*exeName = '\0'; // enter a null in here to separate out the path from the name
	exeName++; // move the pointer forward
	// exePath at this point only contains the path elements and not the name. Change to this directory
	_chdir(exePath);
	return exeName;
}


int startADCApp(int taskId, const std::vector<std::pair<std::string, std::string>> &requestParameters) {
	_chdir(configFile->tasks[taskId]->path);

	// update argv list
	std::string argvUpdated(configFile->tasks[taskId]->argv);

	// make all small caps
	std::transform(argvUpdated.begin(), argvUpdated.end(), argvUpdated.begin(), ::tolower);
	
	// go through arguments and replace with values:
	for (size_t i = 0; i < requestParameters.size(); i++)
	{
		// get query and use small caps to compare
		std::string query = "%" + requestParameters[i].first + "%";
		std::transform(query.begin(), query.end(), query.begin(), ::tolower);

		size_t pos = argvUpdated.find(query);
		if (pos == std::string::npos) continue;
		argvUpdated.replace(pos, query.length(), requestParameters[i].second);
	}

	// TODO: Make this case insensitive
	int result = _spawnl(P_NOWAIT, configFile->tasks[taskId]->exeName, configFile->tasks[taskId]->exeName, argvUpdated.c_str(), NULL);
	if (result == -1){
		printf("Error in startADCApp(). Cannot run the executable (Error code: %d)", errno);
	}
	return result;
}


int invokeDCApp(int testId, char* recvbuf, int maxLength, const std::vector<std::pair<std::string, std::string>> &requestParameters) 
{
    WSADATA wsaData;
    int iResult;

    SOCKET ListenSocket = INVALID_SOCKET;
    SOCKET ClientSocket = INVALID_SOCKET;

    struct addrinfo *result = NULL;
    struct addrinfo hints;

    int recvbuflen = maxLength;
    
    // Initialize Winsock
	printf("Starting up the socket server\n");
    iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
    if (iResult != 0) {
        printf("WSAStartup failed with error: %d\n", iResult);
        return 1;
    }

    ZeroMemory(&hints, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;
    hints.ai_flags = AI_PASSIVE;

    // Resolve the server address and port
    iResult = getaddrinfo(NULL, DEFAULT_PORT, &hints, &result);
    if ( iResult != 0 ) {
        printf("getaddrinfo failed with error: %d\n", iResult);
        WSACleanup();
        return 1;
    }

    // Create a SOCKET for connecting to server
	printf("Listening for clients\n");
    ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
    if (ListenSocket == INVALID_SOCKET) {
        printf("socket failed with error: %ld\n", WSAGetLastError());
        freeaddrinfo(result);
        WSACleanup();
        return 1;
    }

    iResult = bind( ListenSocket, result->ai_addr, (int)result->ai_addrlen);
    if (iResult == SOCKET_ERROR) {
        printf("bind failed with error: %d\n", WSAGetLastError());
        freeaddrinfo(result);
        closesocket(ListenSocket);
        WSACleanup();
        return 1;
    }

    freeaddrinfo(result);

    iResult = listen(ListenSocket, SOMAXCONN);
    if (iResult == SOCKET_ERROR) {
        printf("listen failed with error: %d\n", WSAGetLastError());
        closesocket(ListenSocket);
        WSACleanup();
        return 1;
    }

	// invoke the DC App
	startADCApp(testId, requestParameters);

    // Accept a client socket
    // Setup the TCP listening socket
    ClientSocket = accept(ListenSocket, NULL, NULL);
	printf("Client request has come in, in accept mode\n");
    if (ClientSocket == INVALID_SOCKET) {
        printf("accept failed with error: %d\n", WSAGetLastError());
        closesocket(ListenSocket);
        WSACleanup();
        return 1;
    }

    // No longer need server socket
    closesocket(ListenSocket);

    // Receive until the peer shuts down the connection
	printf("Start receiving client data");
    do {

        iResult = recv(ClientSocket, recvbuf, recvbuflen -1, 0);
        if (iResult > 0) {
            printf("Bytes received: %d\n", iResult);
#if DEBUG_OUTPUT
			printf("received data: %s\n", recvbuf);
#endif
			break;
		} else  {
            printf("recv failed with error: %d\n", WSAGetLastError());
			break;
        }
		//ZeroMemory(recvbuf, recvbuflen);

    } while (1);

    // shutdown the connection since we're done
    iResult = shutdown(ClientSocket, SD_SEND);
    if (iResult == SOCKET_ERROR) {
        printf("shutdown failed with error: %d\n", WSAGetLastError());
    }

    // cleanup
    closesocket(ClientSocket);
    WSACleanup();

    return 0;
}
