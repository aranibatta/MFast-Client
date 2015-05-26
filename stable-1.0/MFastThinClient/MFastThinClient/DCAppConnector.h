#ifndef _DCAPPCONNECTOR_H_
#define _DCAPPCONNECTOR_H_

#include <vector>
#include <string>

int invokeDCApp(int testId, char* recvbuf, int maxLength, const std::vector<std::pair<std::string, std::string>> &requestParameters); 

#endif