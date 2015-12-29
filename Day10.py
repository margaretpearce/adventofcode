import sys

class Day10:
    def processinput(self, input, timestoapply):
        currentinput = input

        for i in range(0,int(timestoapply)):
            workingcopy = ""

            charindex = 0
            currentchar = currentinput[charindex]
            count = 1
            charindex = charindex + 1

            while charindex < len(currentinput):
                if currentinput[charindex] == currentchar:
                    count = count + 1
                else:
                    workingcopy += str(count)
                    workingcopy += currentchar
                    count = 1
                    currentchar = currentinput[charindex]
                charindex = charindex + 1

            workingcopy += str(count)
            workingcopy += currentchar
            currentinput = workingcopy

        print(currentinput)
        print(len(currentinput))


if __name__ == "__main__":
    puzzle = Day10()
    puzzle.processinput(sys.argv[1], sys.argv[2])