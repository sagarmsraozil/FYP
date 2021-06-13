package com.sagarmishra.futsal.interfaces

import com.sagarmishra.futsal.entityapi.FutsalInstances

interface RefreshInstance {
    fun refreshInstances(position:Int,lstInstance:MutableList<FutsalInstances>)
}