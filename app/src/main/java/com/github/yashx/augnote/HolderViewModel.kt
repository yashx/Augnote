package com.github.yashx.augnote

import androidx.lifecycle.ViewModel
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList

class HolderViewModel(private val db: Database) : ViewModel() {

    private val query = db.augnoteQueries

    // query
//    fun getAugRelation(id: Int) = db.getAugRelation(id)
//    fun getAugRelationsForQuery(query: String) = db.getAugRelationsForQuery(query)
    fun getItemsInFolder(folderId: Long) =
//        flow<List<ItemsInFolder>> {
        query.itemsInFolder(folderId).asFlow().mapToList()
//        emit(query.getChildAugRelationForFolder(folderId).executeAsList()
//            .map { ItemsInFolder(it.augRelationId, it.name, "AugRelation") }
//                +
//                query.getChildFolderForFolder(folderId).asFlow().mapToList()
//                    .map { ItemsInFolder(it.folderId, it.name, "Folder") }
//        )

//    }

    // insert
    fun insertFolder(name: String, parentId: Long) = query.insertFolder(name, parentId)
//    fun insertAugRelations(vararg augRelation: AugRelation) = db.insertAugRelations(*augRelation)
//
//    // update
//    fun updateFolders(vararg folder: Folder) = db.updateFolders(*folder)
//    fun updateAugRelations(vararg augRelation: AugRelation) = db.updateAugRelations(*augRelation)
//
//    // delete
//    fun deleteFolders(vararg folder: Folder) = db.deleteFolders(*folder)
//    fun deleteAugRelations(vararg augRelation: AugRelation) = db.deleteAugRelations(*augRelation)

}