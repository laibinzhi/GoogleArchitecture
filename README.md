# GoogleArchitecture

- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- JetPack
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify domain layer data to views.
  - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - dispose observing data when lifecycle state changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - UI related data holder, lifecycle aware.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) Persistence - construct database.
  - [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - display data on recycleView
  - [Navigation](https://developer.android.com/guide/navigation) - one Activity 

- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository pattern
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - dependency injection
  - [SSOT](https://en.wikipedia.org/wiki/Single_source_of_truth) - single-source-of-truth
- Material Design & Animations
- [Retrofit2 & Gson](https://github.com/square/retrofit) - constructing the REST API
- [OkHttp3](https://github.com/square/okhttp) - implementing interceptor, logging and mocking web server
- [Glide](https://github.com/bumptech/glide) - loading images
- ViewPager2




![image](http://lbz-blog.test.upcdn.net/post/google-final-architecture.png)

本实例是参照以上谷歌推荐的应用框架。

常见的应用架构原则有两：
- 分离关注点
- 通过模型驱动界面

以本项目首页为例子，讲述一下整个流程。

## 构建界面

1. 界面由 Fragment *HomeFragment* 及其对应的布局文件 *fragment_home.xml* 组成。
2. 如需驱动该界面，数据模型需要存储以下数据元素：
- Banner列表 *List<Banner>*
- 文章数据 *List<Article>*

我们将使用 *ArticlesitoriesViewModel*（基于 ViewModel 架构组件）存储这些信息。


```
@ExperimentalCoroutinesApi
class ArticlesitoriesViewModel):ViewModel() :
    ViewModel() {

    val articles:LiveData<List<Article>> = TODO()
    val banner:LiveData<List<Banner>> = TODO()

}
```

然后看一下*HomeFragment*


```
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: ArticlesitoriesViewModel by viewModels()
        
}
```

现在我们有了*ArticlesitoriesViewModel*和*HomeFragment*，如何将它们连接起来？毕竟，在 *ArticlesitoriesViewModel* 类中获取 *banner*，我们需要一种方法来通知界面。这时候，JetPack另一个关键成员LiveData就出来了。

LiveData 是一种可观察的数据存储器。应用中的其他组件可以使用此存储器监控对象的更改，而无需在它们之间创建明确且严格的依赖路径。LiveData 组件还遵循应用组件（如 Activity、Fragment 和 Service）的生命周期状态，并包括清理逻辑以防止对象泄漏和过多的内存消耗。

我们将 ArticlesitoriesViewModel 中*的banner*字段类型更改为 LiveData<List<Banner>>。现在，更新数据时，系统会通知 *HomeFragment*。此外，由于此 *LiveData* 字段具有生命周期感知能力，因此当不再需要引用时，会自动清理它们。

现在，我们在*HomeFragment*以观察数据并更新界面：


```
class HomeFragment : BaseFragment<FragmentHomeBinding>(){
   ...
    private val viewModel: ArticlesitoriesViewModel by viewModels()
   ...
   
    private fun getBanner() {
        viewModel?.let { homeModel ->
            homeModel.banner.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.setBanner(it)
                    adapter.notifyDataSetChanged()
                }
            })
        }
        viewModel.getBanner()
    }

}
```

每次获取到banner数据时，系统都会调用 onChanged() 回调并刷新界面。如果您熟悉EventBus或者RxJava等可观察回调的热门框架，我们要手动设置 Fragment 的 onStop() 方法以停止观察数据，否则会出现内存泄漏。使用 LiveData 时没必要执行此步骤，因为它具有生命周期感知能力。这意味着，除非 Fragment 处于活跃状态（即，已接收 onStart() 但尚未接收 onStop()），否则它不会调用 onChanged() 回调。当调用 Fragment 的 onDestroy() 方法时，LiveData 还会自动移除观察者。

在此项目中，在这个机制下，会出现一种现象，数据倒灌。当一个ViewModel里面存在一个LiveData,FragmentA observe 这个LiveData，正常运行，这时候打开一个新的Fragment B，当从FragmentB按返回键，Fragment的回退栈返回，FragmentA重现，会自动走一遍 observe回调，倒灌旧数据。对于数据倒灌的解决，有了很好的解决方法，请参考[该文章](https://www.jianshu.com/p/f3158a4e0bc8)


## 数据获取

现在，我们已使用 LiveData 将 *ArticlesitoriesViewModel* 连接到 *HomeFragment*，那么如何获取banner数据呢？

在本项目中,我们使用Retrofit


```
interface LbzService {
    @GET("/banner/json")
    fun getBannerAsync(): Call<DataResponse<List<Banner>>>
}
```

实现 ViewModel 的第一个想法可能是直接调用LbzService来获取数据，然后将该数据分配给LiveData对象。这种设计行得通，但如果采用这种设计，随着应用的扩大，应用会变得越来越难以维护。这样会使 ArticlesitoriesViewModel 类承担太多的责任，这就违背**了分离关注点**原则。此外，*ViewModel*的时间范围与 Activity 或 Fragment 生命周期相关联，这意味着，当关联界面对象的生命周期结束时，会丢失 Webservice 的数据，进而影响用户体验。

ArticlesitoriesViewModel 会将数据获取过程委派给一个新的模块，即存储区。

存储区模块会处理数据操作。它们会提供一个干净的 API，以便应用的其余部分可以轻松检索该数据。数据更新时，它们知道从何处获取数据以及进行哪些 API 调用。您可以将存储区视为不同数据源（如持久性模型、网络服务和缓存）之间的媒介。

所以我们创建了 *ArticleRepository* 这个类


```
class ArticleRepository @Inject constructor(
    private val service: LbzService) {
     fun getBanner(): LiveData<List<Banner>> {
       // This isn't an optimal implementation. We'll fix it later.
       val data = MutableLiveData<List<Banner>>()
       service.getBannerAsync().enqueue(object : Callback<List<Banner>> {
           override fun onResponse(call: Call<List<Banner>>, response: Response<List<Banner>>) {
               data.value = response.body()
           }
           // Error case is left out for brevity.
           override fun onFailure(call: Call<List<Banner>>, t: Throwable) {
               TODO()
           }
       })
       return data
   }
}
```

虽然存储区模块看起来不必要，但它起着一项重要的作用：它会从应用的其余部分中提取数据源。现在，ArticlesitoriesViewModel 不知道如何获取数据，因此我们可以为视图模型提供从几个不同的数据获取实现获得的数据。

## 连接 ViewModel 与存储区

先说明一下，本项目使用**Hilt**实现依赖注入，它是基于Dagger，相对于Dagger，它更简单使用，重要的是他可以和viewmodel结合使用。想了解更多Hilt的知识，可以参阅[Hilt官方文档](https://developer.android.google.cn/jetpack/androidx/releases/hilt)

我们看回我们最初的ArticlesitoriesViewModel,就是使用依赖注入。

```
@ExperimentalCoroutinesApi
class ArticlesitoriesViewModel @ViewModelInject constructor(private val repository: ArticleRepository) :
    ViewModel() {

    val banner: LiveData<List<Banner>> = repository.getBanner()

}
```

## 缓存和保留数据

目前市面上实现缓存的方式有很多，okhttp CacheControl或者是使用RxCache等，但是这个项目中，打算采用数据库。这时候，JetPack的另一个成员Room就出来了。

要使用 Room，我们需要定义本地架构。首先，我们向 Banner 数据模型类添加 @Entity 注释，并向该类的 id 字段添加 @PrimaryKey 注释。这些注释会将 Banner 标记为数据库中的表格，并将 databaseId 标记为该表格的主键：


```
@Entity(tableName = "banner")
data class Banner(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)
```

然后，我们通过为应用实现 RoomDatabase 来创建一个数据库类：


```
@Database(entities = [Banner::class], version = 1)
abstract class UserDatabase : RoomDatabase()
```
请注意，UserDatabase 是抽象类。Room 将自动提供它的实现。现在，我们需要一种将用户数据插入数据库的方法。为了完成此任务，我们创建一个数据访问对象 (DAO)。


```
@Dao
interface ArticleDao {

   @Insert(onConflict = REPLACE)
   fun insertBanner(banners: List<Banner>)

   @Query("SELECT * FROM banner")
   fun getLocalBanner(): LiveData<List<Banner>>
   
}
```

请注意，getLocalBanner()方法将返回一个 LiveData<List<Banner> 类型的对象。Room 知道何时修改了数据库，并会自动在数据发生更改时通知所有活跃的观察者。由于 Room 使用 LiveData，因此此操作很高效；仅当至少有一个活跃的观察者时，它才会更新数据。

定义 ArticleDao 类后，从我们的数据库类引用该 DAO：


```
@Database(entities = [User::class], version = 1)
abstract class LbzDatabase : RoomDatabase() {
   abstract fun articleDao(): ArticleDao
}
```

#### SSOT(单一可信来源)

不同的 REST API 端点返回相同的数据是一种很常见的现象。例如，如果我们的后端有其他端点返回Banner列表，则同一个用户对象可能来自两个不同的 API 端点，甚至可能使用不同的粒度级别。如果 UserRepository 按原样从 Webservice 请求返回响应，而不检查一致性，则界面可能会显示混乱的信息，因为来自存储区的数据的版本和格式将取决于最近调用的端点。

因此，我们的 ArticleRepository 实现会将网络服务响应保存在数据库中。这样一来，对数据库的更改将触发对活跃 LiveData 对象的回调。使用此模型时，数据库会充当单一可信来源，应用的其他部分则使用 ArticleRepository 对其进行访问。无论您是否使用磁盘缓存，我们都建议您的存储区将某个数据源指定为应用其余部分的单一可信来源。


现在，我们可以修改 ArticleRepository 纳入Room,并且将它设置唯一可信来源：


```

class ArticleRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {

    val banner: LiveData<List<Banner>> = database.articleDao().getLocalBanner()

    suspend fun getBanner() {
          try {
                val bannerFromNet = service.getBannerAsync()
                //判断从网络上获取的和从数据库获取的是否相同，相同的就不返回了
                val bannerFromDb = database.articleDao().getLocalBannerNotLiveData()
                if (!CollectionUtil.compareTwoList(bannerFromDb, bannerFromNet.data)) {
                    database.articleDao().clearBanner()
                    database.articleDao().insertBanner(bannerFromNet.data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
}
```

相对应，ArticlesitoriesViewModel改为

```
@ExperimentalCoroutinesApi
class ArticlesitoriesViewModel @ViewModelInject constructor(private val repository: ArticleRepository) :
    ViewModel() {
    
    val banner: LiveData<List<Banner>> = repository.banner

    fun getBanner() {
        viewModelScope.launch {
            repository.getBanner()
        }
    }
}

```
HomeFragemnt通过监听ArticlesitoriesViewModel中的 LiveData<List<Banner>> 来更新UI,通过调用getBanner()实现数据的加载。

以上就是整个项目中获取Banner数据的一个小例子。

## Paging

分页加载是Android开发中很常见的需求，我们需要以列表的形式加载大量的数据，来自net或者db，Paging就是Google为了方便开发者完成分页加载而设置的一个组件。Paging支持三种架构类型，分别是网络数据，数据库，网络数据+数据库。

目前官方正式版是Paging2，但是我更喜欢还在beta版本的Paging3，因为相对于Paging2，可以检测列表数据的实时状态，另外还提供刷新和重试机制等

使用Paging3的好处

1. 可以缓存分页数据，可确保应用在处理页面数据时有效的使用系统资源
2. 内置请求重复数据删除的功能，确保您的应用有效的使用宽带网络和资源系统
3. 可配置的 RecyclerView 适配器，当用户滚动到加载的数据的末尾时会自动请求数据。
4. 对Kotlin协程和Flow以及LiveDataRxJava 的一流支持 。
5. 内置的错误处理支持，包括刷新和重试功能。
提供反馈意见

![image](http://lbz-blog.test.upcdn.net/post/paging3-library-architecture.svg)


下面，以首页的列表数据为例子，讲述一下分页列表的实现。

1. PagingDataAdapter 适配器

首先编写Adapter,需要继承PagingDataAdapter，注意一下需要实现系统方法

```
   companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
```

2. 编写ArticleRepository,如果只是实现网络数据的加载，不使用数据库，直接使用下面

```
class ArticlePagingSource(private val service: LbzService) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val response = service.getArticles(position)
            val repos = response.data.datas
            LoadResult.Page(
                data = repos,
                prevKey = null,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
```

然后再ArticleRepository调用


```
class ArticleRepository @Inject constructor(
    private val service: LbzService
) {
    fun getArticles(articleType: Int): Flow<PagingData<Article>> {
        _articleType = articleType
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2 * NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { ArticlePagingSource(service)}        
        ).flow
    }
}
```

这样就可以实现一个单纯的网络分页加载的。如果需要实现一个网络加载+数据库缓存的分页列表，就要使用到Paging3 中RemoteMediator这个类。具体参考


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


上面的就是ArticleRemoteMediator中网络和数据库的一些操作，简单说一下，可以通过LoadType获取状态，他有三个，REFRESH，PREPEND，APPEND。根据不同状态获取对应的页码。然后读取数据库文章列表的数目，这个的作用就是如果数目数据库不为空，那么在离线的情况下，列表也可以正常显示数据，如果为空，那么就显示出错信息。然后获取接口请求数据，获取成功之后，如果是刷新。那么就删除本地数据库，插入最新的数据。此处，用了一个数据库表储存加载的页码（上一页，下一页）


然后同样在ArticleRepository调用如下


```
class ArticleRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {
    private val pagingSourceFactory = { database.articleDao().getLocalArticles(_articleType) }

   fun getArticles(articleType: Int): Flow<PagingData<Article>> {
        _articleType = articleType
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2 * NETWORK_PAGE_SIZE
            ),
            remoteMediator = ArticleRemoteMediator(
                service,
                database,
                articleType
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}
```


## Navigation
程序使用谷歌推荐的单Activity多Fragment模式，本实例使用JetPack中的Navigation组件结合底部BottomNavigationView实现页面导航，部分页面采用ViewPager2+TabLayout的布局实现滑动，如项目列表。

## Kotlin 协程Coroutine
项目中多处使用kotlin协程,例如，在首页监听列表数据的时候


```
   private var job: Job? = null

   private fun getArticles() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getArticles().collectLatest {
                adapter.submitData(it)
            }
        }
    }

```
例如在ViewModel中使用作用域

```
@ExperimentalCoroutinesApi
class ArticlesitoriesViewModel @ViewModelInject constructor(private val repository: ArticleRepository) :
    ViewModel() {

    fun getArticles(): Flow<PagingData<Article>> = repository.getArticles(ArticleType.HOME_ARTICLE)
        .cachedIn(viewModelScope)

    fun getBanner() {
        viewModelScope.launch {
            repository.getBanner()
        }
    }
}
```

还比如在Retrofi中使用协程两种方式：

1. 直接使用挂起函数

```
  @GET("/article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): DataResponse<PageBean<Article>>
```

2. 为原有Call类型添加await等类似的扩展以将其转成挂起函数


```

    @GET("/banner/json")
    fun getBannerAsync(): Deferred<DataResponse<List<Banner>>>
```


```
    val bannerFromNet = service.getBannerAsync().await()

```






## 注意

接口数据是使用[鸿洋的网站开放API](https://www.wanandroid.com/blog/show/2)

页面UI部分参考(https://github.com/hegaojian/JetpackMvvm)


## 运行截图
![image](https://s31.aconvert.com/convert/p3r68-cdx67/pu86h-6n1cj.gif)


## 最后

项目地址：https://github.com/laibinzhi/GoogleArchitecture





