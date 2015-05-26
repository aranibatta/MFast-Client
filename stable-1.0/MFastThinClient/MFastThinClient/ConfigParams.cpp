#include "configParams.h"
#include <Windows.h>
#include <tchar.h>
#include <stdio.h>
#include <process.h>
#include <direct.h>

#define PROCESSING_GENERAL_INFO		1
#define PROCESSING_TASKS			2


ConfigFile *configFile ;

char* getParameterValue(char *parameter){
	char *temp = parameter;
	char *value;
	// move forward in the string until we get =
	while(*temp != '=')temp++;
	temp++;
	value = (char*)calloc(1, strlen(temp) + 1);
	strncpy(value, temp, strlen(temp));
	return value;
}

int startsWith(const char *pre, const char *str)
{
    size_t lenpre = strlen(pre),
           lenstr = strlen(str);
	if(lenstr > lenpre)return 0;
	if(strncmp(pre, str, lenstr))return 0;
	return 1;
}

int readConfigFile(char *configFileName){
	int taskNumber = 0;
	char buff[1024];
	char *line;
	char *temp;
	memset(buff, 0, 1024);
	FILE *fp;
	int processingState = PROCESSING_GENERAL_INFO;

	configFile = (ConfigFile*)calloc(1, sizeof(ConfigFile));

	fopen_s(&fp, configFileName, "r");

	if(fp == NULL){
		printf("Could not open file %s", configFileName);
		return 0;
	}
	while(fgets(buff, 1024, fp) != NULL){
		line = buff;
		{
			// remove spaces
			// remove leading spaces
			while(*line == ' ')line++;
			// remove trailing spaces
			temp = line + strlen(line) - 1;
			while(*temp == ' ' || *temp == '\n')temp--;
			*(temp + 1) = 0;
		}

		if(!strlen(line))continue; // empty line
		if(*line == ';')continue; // comment line

		// parsing the parameters
		switch(processingState){
			case PROCESSING_GENERAL_INFO:
				if(startsWith(line, "num_of_tasks")){
					char* numTasks = getParameterValue(line);
					configFile->numOfTasks = atoi(numTasks);
					free(numTasks);
					// allocate an array of task infos
					configFile->tasks = (TaskInfo**)calloc(configFile->numOfTasks, sizeof(TaskInfo*));
				}else if(startsWith(line, "[Task")){
					processingState = PROCESSING_TASKS;
				}
				break;
			case PROCESSING_TASKS:
				char* value = getParameterValue(line);
				if(startsWith(line, "tag")){
					configFile->tasks[taskNumber] = (TaskInfo*)calloc(1, sizeof(TaskInfo));
					configFile->tasks[taskNumber]->tag = value;
				}else if(startsWith(line, "bmp")){
					configFile->tasks[taskNumber]->bmpFileName = value;
				}else if(startsWith(line, "path")){
					configFile->tasks[taskNumber]->path = value;
				}else if(startsWith(line, "exe")){
					configFile->tasks[taskNumber]->exeName = value;
				}else if(startsWith(line, "argv")){
					configFile->tasks[taskNumber]->argv = value;
					taskNumber++;
				}
				break;
		}
	}
	fclose(fp);
	return 1;
}
