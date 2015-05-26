#include "Poco/Net/StreamSocket.h"
#include "Poco/Net/SocketStream.h"
#include "Poco/Net/SocketAddress.h"
#include "Poco/StreamCopier.h"
#include "Poco/Path.h"
#include "Poco/Exception.h"
#include "Poco/Net/HTTPClientSession.h"
#include "Poco/Net/HTTPRequest.h"
#include "Poco/Net/HTTPMessage.h"
#include "Poco/Net/HTTPResponse.h"
#include "Poco/Net/HTMLForm.h"
#include "Poco/Net/TCPServerConnection.h"
#include "Poco/Net/TCPServer.h"
#include "Poco/Net/TCPServerConnectionFactory.h"
#include "Poco/Net/ServerSocket.h"
#include "Poco/Net/ServerSocketImpl.h"
#include "DCAppConnector.h"
#include "configParams.h"
#include <iostream>


#define WIN32_LEAN_AND_MEAN

using Poco::Net::StreamSocket;
using Poco::Net::SocketStream;
using Poco::Net::SocketAddress;
using Poco::StreamCopier;
using Poco::Path;
using Poco::Exception;
using Poco::Net::TCPServerConnection;
using Poco::Net::TCPServerConnectionFactoryImpl;
using Poco::Net::ServerSocket;
using Poco::Net::ServerSocketImpl;

using Poco::Net::TCPServer;

#include "stdafx.h"

#define RESPONSE_BUFFER_DEFAULT_SIZE 0x10000000

static HANDLE mainThread;

class ThinAppServerConnection: public TCPServerConnection {
public:
	ThinAppServerConnection(const StreamSocket& s): TCPServerConnection(s) { }
	void run() {
		char* responseHeader = "HTTP/1.0 200 OK\nContent-Type: text/html\nContent-Length:";
		char *responseBuff = (char*)malloc(RESPONSE_BUFFER_DEFAULT_SIZE);
		char *responseHeaderBuffer = (char*)malloc(strlen(responseHeader) + 10);
		int responseLen;
		StreamSocket& ss = socket();
		try {
			char buffer[1024];
			int n = ss.receiveBytes(buffer, sizeof(buffer));
			while (n > 0) {
				std::cout << "-------------------------" << std::endl;
				std::cout << "ThinAppServerConnection: Waiting for request." << std::endl;
				std::cout << std::endl;

				handleRequest(buffer, responseBuff);
				responseLen = strlen(responseBuff);

				sprintf(responseHeaderBuffer, "%s %d \n\n", responseHeader, responseLen);
				int responseHeaderLength = strlen(responseHeaderBuffer);
				// shift the response forward by the header length
				memmove(responseBuff + responseHeaderLength, responseBuff, responseLen);
				// append the header
				memcpy(responseBuff, responseHeaderBuffer, responseHeaderLength);
				responseLen += responseHeaderLength;
				int sentLength = ss.sendBytes(responseBuff, responseLen);
				if(sentLength < 0){
					printf("\n Could not send data \n");
				}else{
					printf("\n Sent %d bytes of data \n", strlen(responseBuff));
				}

				memset(buffer, 0, 1024);
				memset(responseBuff, 0, RESPONSE_BUFFER_DEFAULT_SIZE);

				n = ss.receiveBytes(buffer, sizeof(buffer));
			}
		}catch (Poco::Exception& exc)
		{
			std::cerr << "ThinAppServer: " << exc.displayText() << std::endl; 
		}

		// Saqib... why were you not freeing memory buffers here????!
		if (responseBuff)
		{
			free(responseBuff);
			responseBuff = NULL;
		}

		if (responseHeaderBuffer)
		{
			free(responseHeaderBuffer);
			responseHeaderBuffer = NULL;
		}
	}

	char *getParameterValue(const char *requestBuffer, const char *paramName){
		const char *requestParams = strchr(requestBuffer, '?');

		const char *testIdParameter = strstr(requestBuffer, "?testId=");

	}

	char *getRequestParameterValue(const char *requestBuffer, const char *pName){
		char *value = (char *)malloc(strlen(requestBuffer) + 1);
		char *paramName = (char *)malloc(strlen(pName) + 3);
		sprintf(value, "%s", requestBuffer);
		sprintf(paramName, "?%s=", pName);
		const char * paramValuePair = strstr(value, paramName);
		if(paramValuePair == NULL){
			sprintf(paramName, "&%s=", pName);
			paramValuePair = strstr(value, paramName);
			if(paramValuePair == NULL){
				return NULL;
			}
		}
		sprintf(value, "%s", ++paramValuePair);
		char *temp = strchr(value, '&');
		if(temp){
			*temp = '\0';
		}
		temp = strchr(value, ' ');
		if(temp){
			*temp = '\0';
		}
		value = strchr(value, '=');
		value++;

		if (paramName)
		{
			free(paramName);
			paramName = NULL;
		}
		return value;
	}

	/**
	*	Function extracts value of the parameter in the provided list. This function replaces Saqib's function that causes memory leak.
	*/
	std::string getRequestParameterValue(const std::vector<std::pair<std::string, std::string>> &parameterList, const std::string parameterName)
	{
		std::string param2 = parameterName;
		std::transform(param2.begin(), param2.end(), param2.begin(), ::tolower);

		for (size_t i = 0; i < parameterList.size(); i++)
		{
			std::string param1 = parameterList[i].first;
			std::transform(param1.begin(), param1.end(), param1.begin(), ::tolower);

			if (param1.compare(param2) == 0)
				return parameterList[i].second;
		}

		return "";
	}

	/**
	*	Converts request buffer into list of pairs of [parameterName, parameterValue]
	*/
	std::vector<std::pair<std::string, std::string>> getRequestParameterList(const char *requestBuffer)
	{

		std::vector<std::pair<std::string, std::string>> params;

		std::string str (requestBuffer);
		std::size_t found = str.find_first_of("?");
		std::size_t stop = str.find("HTTP/");
		str = str.substr(0, stop);

		while (found!=std::string::npos)
		{
			std::size_t found2 = str.find_first_of("&", found+1);
			if (found2 == std::string::npos)
			{
				found2 =  str.find_first_of(" ", found+1);
				if (found2 == std::string::npos)
					found2 = str.size() - 1;
			}

			if (found2 == found) break;

			size_t len = found2 - found - 1;
			std::string tmp = str.substr(found +1, len);
			//std::cout << str.substr(found +1, len) << std::endl;
			//std::cout << tmp << std::endl;

			// store
			size_t split = tmp.find_first_of("=");
			if (split == std::string::npos) continue;

			std::string name = tmp.substr(0, split);
			std::string value = tmp.substr(split+1, str.size()-split);

			params.push_back(std::make_pair<std::string, std::string>(name, value));

			found = found2;
		}


		return params;
	}

	/**
	*	Function handles request from the requestBuffer and invokes DCApp with appropriate paramters
	*/
	void handleRequest(const char *requestBuffer, char* responseBuffer){

		int result = -1;
		// determine the test id
		//char *testIdParameter = getRequestParameterValue(requestBuffer, "testid");

		// get the request parameters arranged into pairs of [parameterName, parameterValue]
		auto requestParameters = getRequestParameterList(requestBuffer);

		// extract test ID
		std::string testIdParameter = getRequestParameterValue(requestParameters, "testid");

		if(testIdParameter.empty()) 
		{
			// failed
			sprintf(responseBuffer, "Invalid request format");
			return;
		}
		int testId = atoi(testIdParameter.c_str());

		memset(responseBuffer, 0, RESPONSE_BUFFER_DEFAULT_SIZE);
		// determine the request id
		result = invokeDCApp(testId, responseBuffer, RESPONSE_BUFFER_DEFAULT_SIZE, requestParameters);
		if(result){
			// failed
			sprintf(responseBuffer, "Data collection failed");
		}

	}
};


int main(int argc, char** argv)
{
	if(argc < 2){
		printf("Usage: FivePlusThinClient <config file path and name>");
		return 1;
	}

	if(!readConfigFile(argv[1])){
		printf("Configuration file %s not found\n", argv[1]);
		printf("Usage: FivePlusThinClient <config file path and name>");
		return 1;
	}

	// start the server
	mainThread = OpenThread(THREAD_ALL_ACCESS, FALSE, GetCurrentThreadId());
	ServerSocket svs(55780);
	TCPServer srv(new TCPServerConnectionFactoryImpl<ThinAppServerConnection>(),svs);
	srv.start();
	std::cout << "ThinAppServer Running. " << std::endl;

	// suspend the main thread so that the server may continue to run
	SuspendThread(mainThread);
	srv.stop();
	return 0;
}