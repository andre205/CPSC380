#Tyler Andrews Sudoku Solution Checker
#CPSC380 Project 1

import threading, sys
from threading import Thread

#Given a file name, open it, and read its lines to create a sudoku grid
def readFile(filename):
	f = open(filename, "r")
	lines = f.readlines()
	f.close()
	G = []
	for line in lines:
		splitLine = line.replace(" ","").replace("\n","").split(",")
		l = []
		for entry in splitLine:
			try:
				l.append(int(entry))
			except:
				continue
		G.append(l)
	return G

#Given a sudoku grid, output array, and output index, iterate through
#each row in the grid and check that it contains elements 1-9
#If there are any duplicates, append their indices as tuples
#to the output array at the given output index
def check_rows(G, out, ind):
  errors = []
  for i in range(0,9):
    elements = []
    dupCol = -1
    for j in range(0,9):
      if (G[i][j] not in elements):
        elements.append(G[i][j])
      else:
        dupCol = j
        #print("Row check failed")
        #print("Duplicate in row", i+1, "col", j+1)
        for k in range(9):
            if (G[i][k] == G[i][dupCol]):
                errors.append([i,k])
  out[ind] = errors

#check_row method altered for column checking
def check_columns(G, out, ind):
  errors = []
  for i in range(0,9):
    elements = []
    dupRow = -1
    for j in range(0,9):
      if (G[j][i] not in elements):
        elements.append(G[j][i])
      else:
        dupRow = j
        #print("Col check failed")
        #print("Duplicate in row", j+1, "col", i+1)
        errors.append([j,i])
        for k in range(9):
            if (G[k][i] == G[dupRow][i]):
                errors.append([k,i])
  out[ind] = errors

#check_row method altered for 3x3 subgrid checking
def check_subgrids(G, out, ind):
  errors = []
  tempRow = []
  elements = []
  for i in range(0,3):
    for j in range(0,3):
      if (G[i][j] not in elements):
        elements.append(G[i][j])
      else:
        errors.append([i,j])

  elements = []
  for i in range(0,3):
    for j in range(3,6):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(0,3):
    for j in range(6,9):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(3,6):
    for j in range(0,3):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(3,6):
    for j in range(3,6):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(3,6):
    for j in range(6,9):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(6,9):
    for j in range(0,3):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(6,9):
    for j in range(3,6):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  elements = []
  for i in range(6,9):
    for j in range(6,9):
      if (G[i][j] not in elements):
          elements.append(G[i][j])
      else:
          errors.append([i,j])

  out[ind] = errors

#Implement 3 threads to check all rows, columns, and subgrids of a sudoku grid
#Append the error arrays from each test to a results array, which
#is returned via finalout. These error arrays are then used by fixGrid
def check_sudoku(S, finalout):
	threads = [None] * 3
	results = [None] * 3
	t1 = Thread(target=check_rows, args=(S, results, 0))
	t2 = Thread(target=check_columns, args=(S, results, 1))
	t3 = Thread(target=check_subgrids, args=(S, results, 2))
	t1.start()
	t2.start()
	t3.start()
	t1.join()
	t2.join()
	t3.join()
	finalout.append(results[0])
	finalout.append(results[1])
	finalout.append(results[2])
	#print(results[0])
	#print(results[1])
	#print(results[2])

#Given a sudoku grid, and arrays of indeces of duplicate elements
#for each set of rows, colums, and subgrids, find indeces of
#duplicates that overlap all 3 test cases (row, col, sg)
#and indicate to the user what value that duplicate should be changed to
def fixGrid(G, rowErrors, colErrors, sgErrors):
	errorsPrinted = 0
	if len(rowErrors)==0:
		if len(colErrors)==0:
			if len(sgErrors)==0:
				print("Working solution")
	else:
		print("Errors found")
	for elem in rowErrors:
		if elem in colErrors:
			if elem in sgErrors:
				print("The value at: row", elem[0]+1, "col", elem[1]+1)
				allElem = [1,2,3,4,5,6,7,8,9]
				for i in range(9):
					index = elem[0]
					if G[index][i] in allElem:
						allElem.remove(G[index][i])
				if errorsPrinted > 0:
					print("Should be:", allElem[1])
				else:
					print("Should be:", allElem[0])
					errorsPrinted = errorsPrinted + 1

###########################################

#Given a file name as a command line argument, open the file,
#read the sudoku grid, check its validity, and indicate to the
#user how to fix it if it is invalid
try:
	S1 = readFile(sys.argv[1])
	finalout = []
	check_sudoku(S1, finalout)
	fixGrid(S1, finalout[0], finalout[1], finalout[2])
except:
	print("Invalid file")
	print("This program takes a text file containing a sudoku grid as a command line argument.")
