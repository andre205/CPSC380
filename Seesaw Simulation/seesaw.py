import threading
import asyncio
import time

a = asyncio.Semaphore(value=1, loop=None)
b = asyncio.Semaphore(value=0, loop=None)

def fred(s):
  a.acquire()
  print("Fred push")
  while(s[1]>1):            #Move until wilma reaches 1
    s[0] = s[0] + 1
    s[1] = s[1] - 1
    print("Fred: " + str(s[0]) + "      Wilma: " + str(s[1]))
    time.sleep(.1)
  b.release()

def wilma(s):
  b.acquire()
  print("Wilma push")
  while(s[0]>1):            #Move until fred reaches 1
    s[0] = s[0] - 1.5
    s[1] = s[1] + 1.5
    print("Fred: " + str(s[0]) + "      Wilma: " + str(s[1]))
    time.sleep(.1)
  s[2] = s[2] - 1           #wilma has completed an iteration of the simulation
  a.release()

def simulate(iterations):
  i = 1                     #Iteration counter
  sa = [1.0,7.0,iterations] #Simulation array in the form [fred height, wilma height, iterations left]
  print("Start")
  print("Fred: " + str(sa[0]) + "      Wilma: " + str(sa[1]))
  while(sa[2] > 0):         #Simulate until no more iterations remaining
    print("Iteration " + str(i))
    threads = []
    tf = threading.Thread(target=fred(sa))
    tw = threading.Thread(target=wilma(sa))
    threads.append(tf)
    threads.append(tw)
    tf.start()
    tw.start()
    print()
    i = i+1

simulate(10)
