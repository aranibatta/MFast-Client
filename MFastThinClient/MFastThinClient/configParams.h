#ifndef __CONFIG_PARAMS_H__
#define __CONFIG_PARAMS_H__


typedef struct _taskInfo{
	char *tag;
	char *bmpFileName;
	char *path;
	char *exeName;
	char *argv;
}TaskInfo;

typedef struct _ConfigFile{
	int saveParam;
	char *sourceFileName;
	char *resultsFileName;
	char *iniFile; // settings file name;
	int numOfTasks;
	TaskInfo **tasks;
}ConfigFile;

int readConfigFile(char *configFileName);

#endif // __CONFIG_PARAMS_H__