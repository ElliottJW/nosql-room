package dev.libatorium.nosqlroom.query

interface DbModelQueryable {

    /**
     * A set of queryable fields that can be used to identify a specific DbModel
     */
    val queryableFields: Set<DbModelQueryableField>?
}