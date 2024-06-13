package com.treevalue.robot.resource.global

object RemoteAddress {
//    MG: mongodb, PG: postgresql
    val PY_SEMITICS_HTTP = "http://localhost:12000/search?q="
    val MONGO_PREVIEW_BY_RESOURCEID = "http://localhost:10003/treevalue/robot/resource/preview/{id}"
    val PG_RESOURCE_PRICE_EVALUATION="http://localhost:10003/treevalue/robot/resource/priceAndEvaluation/{resourceId}"
    val PG_RESOURCE_FEEDBACK_EVALUATION="http://localhost:10003/treevalue/robot/resource/feedbackLevel/{resourceId}"
}
