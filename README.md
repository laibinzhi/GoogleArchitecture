# GoogleArchitecture
Architecture Components+Jetpack+Room+Paging3+Retrofit+DataBinding+ViewModel+LiveData+Kotlin

![image](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

基于以上架构写了一个简单的Demo，用了kotlin语言。

接口数据是使用[鸿洋的网站开放API](https://www.wanandroid.com/blog/show/2)中获取文章列表的接口，实现了一个文章列表的功能

程序入口MainActivty和唯一页面ArticleFragment使用了Jetpack中[navigation](https://developer.android.com/guide/navigation/)的组件

fragment监听viewModel中的列表PagingData数据变化，该数据唯一指向Room数据库。在此，这里很喜欢谷歌推荐单一数据源的方式。使用单一源，可以拥有一个离线应用程序，并确保数据始终使用一个来源，即数据库。这样的好处就可以很好解决多数据源处理起来的麻烦。说明白点，就是列表数据只来自Room数据库，当Room数据库为空或者到达临街条件的时候，它就会通过RemoteMediator这个类，去请求网络数据，请求网络数据成功之后保存到数据库，然后列表监听到数据库的变化去加载显示，此处，Paging+Room+LiveData的好处就体现出来。他们都支持LiveData或者RxJava。其实我之前也常用RxJava，但是LiveData这个东西和ViewModel结合使用，可以绑定activity或者fragment的生命周期。


```
class ArticleRemoteMediator(
    private val service: ArticleService,
    private val database: ArticleDatabase
) : RemoteMediator<Int, Article>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {

        var page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: ARTICLE_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        var localArticleSize = 0 //本地数据库数据大小

        database.withTransaction {
            localArticleSize = database.articleDao().getLocalArticleSize()
        }

        try {
            val apiResponse = service.getArticles(page)

            val articles = apiResponse.data.datas
            val endOfPaginationReached = articles.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.articleDao().clearArticles()
                }
                val prevKey = if (page == ARTICLE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(articleId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.articleDao().insertAll(articles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return if (loadType == LoadType.REFRESH) {
                if (localArticleSize > 0) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            } else {
                MediatorResult.Error(exception)
            }
        } catch (exception: HttpException) {
            return if (loadType == LoadType.REFRESH) {
                if (localArticleSize > 0) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            } else {
                MediatorResult.Error(exception)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                database.remoteKeysDao().remoteKeysArticleId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                database.remoteKeysDao().remoteKeysArticleId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysArticleId(repoId)
            }
        }
    }

}
```

上面的就是网络和数据库的一些操作，简单说一下，可以通过LoadType获取状态，他有三个，REFRESH，PREPEND，APPEND。根据不同状态获取对应的页码。然后读取数据库文章列表的数目，这个的作用就是如果数目数据库不为空，那么在离线的情况下，列表也可以正常显示数据，如果为空，那么久显示出错信息。然后获取接口请求数据，获取成功之后，如果是刷新。那么就删除本地数据库，插入最新的数据。此处，用了一个数据库表储存加载的页码（上一页，下一页）



列表使用Pagging3，相对pagging2,他加入了一些加载状态，可以检测列表数据的实时状态，另外还提供刷新和重试机制，确实好用

此外，项目使用了基于Dagger的Hilt依赖注入框架，相对于Dagger，它更简单使用，重要的是他可以和viewmodel结合使用

此项目是第一次使用kotlin练手。和java开发相比，确实是少了很少代码。项目中也有用到kotlin的协程，对于一些异步操作数据库操作都是很好。

项目地址：https://github.com/laibinzhi/GoogleArchitecture









