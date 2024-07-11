package com.treevalue.robot.memeryriver

class Relation {
    /*
                [mk1][mk2][mk3][mk10][mk11][mk12]
    [mark1]  true false ...
    [mark2] false ...
    [mark3] t ...
    [mark10] f...
    [mark11] ...
    [mark12]...
    from is mark set as axis , and conns express mark n and mark m whether have connections,
    true is has connection
     */
//    whether has connection in tow mark in row and column position
    lateinit var conns: Array<Array<Boolean>>

    //    mark set , form a array as axis for connection matrix
    lateinit var from: HashSet<Long>
}
