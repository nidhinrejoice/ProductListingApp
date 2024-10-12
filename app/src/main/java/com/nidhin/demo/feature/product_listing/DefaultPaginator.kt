package com.nidhin.demo.feature.product_listing

import com.nidhin.demo.domain.models.ProductDomainModel


interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()

}
class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<ProductDomainModel>>,
    private inline val getNextKey: suspend (List<ProductDomainModel>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<ProductDomainModel>, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isRequestInProgress = false
    override suspend fun loadNextItems() {
        if (isRequestInProgress) {
            return
        }
        isRequestInProgress = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isRequestInProgress = false
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}