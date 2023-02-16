import random
import numpy

network =  {1:  [2, 20],   # Directed acyclic graph adjacency list
            2:  [1, 5],
            3:  [4],
            4:  [3, 5],
            5:  [2, 4, 6],
            6:  [5, 19],
            7:  [8],        # Attacker 2
            8:  [7, 9],
            9:  [8, 10],
            10: [9, 19],
            11: [12],       # Attacker 1
            12: [11, 13],
            13: [12, 14],
            14: [13, 19],
            15: [16],
            16: [15, 17],
            17: [16, 18],
            18: [17, 19],
            19: [6, 10, 14, 18],
            20: [1]}

branch1 = [20, 1, 2, 5, 6, 19]
branch2 = [3, 4, 5, 6, 19]
branch3 = [7, 8, 9, 10, 19]     # Attacker 2
branch4 = [11, 12, 13, 14, 19]  # Attacker 1
branch5 = [15, 16, 17, 18, 19]

packets = [] #  master list of packets that have arrived at the victim
paths = []  # all found full and partial network paths
final_paths = [] # all found full paths
branches = [0, 0, 0, 0, 0] # tally of branch "weight"
num_pkts = 1

x = 10 #  10, 100, 1000
p = 0.2  #  0.2, 0.4, 0.5, 0.6, 0.8

class Packet:
    def __init__(self):
        self.node = None # = "unmarked"
        self.start = None
        self.end = None
        self.distance = None

def edgeSampling_marking(branch, pkt):
    global packets
    i = 0
    while i < len(branch):                          #  for each node/router in the branch
        k = random.uniform(0, 1)                    #  x (aka k) is a random # from 0-1
        if k < p:                                   #  if x < p then,
            pkt.start = branch[i]                   #  write the router into pkt.start 
            pkt.distance = 0                        #  and set pkt.distance to 0
        else:
            if pkt.distance == 0:
                pkt.end = branch[i]                 #  write the router into pkt.end
            pkt.distance = 0                        # need to "mark" and begin at 0 in order to increment
            pkt.distance += 1                       #  increment the distance
        packets.append(pkt)                         #  add every packet to master list of packets that have arrived at the victim
        i += 1

def path_check(pkt):
    global packets, final_paths, branches
    i = 0
    for path in final_paths: # for all possible linear paths
        if pkt.start in path and pkt.end in path:
            branches[i] += 1
        i += 1

def edge_path_reconstruction():
    global packets
    for pkt in packets:                 # remove all unmarked packets
        if pkt.distance == None:
            print(pkt.distance)
            packets.remove(pkt)
        path_check(pkt)                 # test the path they came from

def depthFirst(graph, currentVertex, visited):
    visited.append(currentVertex)
    for vertex in graph[currentVertex]:
        if vertex not in visited:
            depthFirst(graph, vertex, visited.copy())
    global paths
    paths.append(visited)


def main():
    global network, paths, final_paths, packets, branches
    successes = 0

    depthFirst(network, 19, []) # find all possible linear paths within the network
    for branch in paths:
        if len(branch) >= 5:
            final_paths.append(branch)
    final_paths.pop(1)
    
    for number in range(100):
        for number in range(num_pkts):
            pkt = Packet()
            edgeSampling_marking(branch1, pkt)
            edgeSampling_marking(branch2, pkt)
            edgeSampling_marking(branch3, pkt)
            edgeSampling_marking(branch5, pkt)

        for number in range(num_pkts*x):
            pkt = Packet()
            edgeSampling_marking(branch4, pkt)

        edge_path_reconstruction()

        max_value = numpy.max(branches)
        attacker = branches.index(max_value) + 1
        if attacker == 4:
            successes +=1

        packets.clear()             # reset/clear all sent packets
        branches = [0, 0, 0, 0, 0]  # reset/clear all sent paths
        # print('known attacker path: ' + str(numpy.array2string(numpy.flip(branch4), separator=", ")))
        # print('found attacker path: ' + str(final_paths[attacker-1]))
    print(str(successes) + ' out of 100 edgeSampling tests were successful.')

if __name__ == "__main__":
    main()