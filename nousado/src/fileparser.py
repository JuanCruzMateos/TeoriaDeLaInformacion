import sys
import re
import math
from pprint import pprint


class FileParser:
    def __init__(self, inputfile, digitos_palabra):
        self.__inputfile = inputfile
        self.__outputfile = None
        self.__digitos_palabra = digitos_palabra
        self.__events = {}
        self.__frec = {}
        self.__prob = {}
        self.__cant_info = {}


    def parsefile(self):
        regex = "." * self.__digitos_palabra
        with open(self.__inputfile, 'r') as txt:
            file_content = txt.read()
        self.__events = re.findall(regex, file_content)
        for e in self.__events:
            self.__frec[e] = self.__frec.get(e, 0) + 1
        total = sum(self.__frec.values())
        for key in self.__frec.keys():
            self.__prob[key] = self.__frec[key] / total
        for key in self.__frec.keys():
            self.__cant_info[key] = FileParser.cant_info(self.__prob[key])

    
    def entropia(self) -> float:
        h = 0
        for event in self.__cant_info.keys():
            h += self.__prob[event] * self.__cant_info[event]
        return h


    def getCantInfo(self):
        return self.__cant_info


    def cant_info(prob: float) -> float:
        return math.log(prob, 2) * -1



def main(args):
    fileparser = FileParser(args[1], int(args[2]))
    fileparser.parsefile()
    pprint(fileparser.getCantInfo())
    print(f"H(s) = {fileparser.entropia()}")


if __name__ == '__main__':
    main(sys.argv)

