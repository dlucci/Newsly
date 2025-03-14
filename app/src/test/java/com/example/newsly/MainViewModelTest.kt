import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.newsly.database.NewslyDatabase
import com.example.newsly.database.TopStoryDao
import com.example.newsly.model.Results
import com.example.newsly.model.TopStories
import com.example.newsly.repository.NewsRepository
import com.example.newsly.viewmodel.MainViewModel
import com.example.newsly.viewmodel.NewsState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule() // Ensures LiveData works properly

    private val mockRepository = mockk<NewsRepository>()
    private val mockDatabase = mockk<NewslyDatabase>()
    private val fakeDbFlow = MutableStateFlow(emptyList<TopStories>()) // No data in DB initially
    private val fakeNetworkFlow = MutableSharedFlow<Result<Results>>() // Will emit test values
    private val fakeTopStoryDao = mockk<TopStoryDao>()
    private val fakeTopStories = flowOf<List<TopStories>>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should emit Loading, LocalSuccess, then RemoteSuccess on successful API call`() = runTest {

        coEvery { mockDatabase.topStoriesDao() } returns fakeTopStoryDao
        coEvery { mockDatabase.topStoriesDao().getStories() } returns fakeTopStories
        coEvery { mockDatabase.topStoriesDao().insertStories(any()) } just Runs

        coEvery { mockDatabase.topStoriesDao().getStories() } returns fakeDbFlow

        val fakeNetworkFlow = MutableSharedFlow<Result<Results>>() // Simulate API

        val fakeNewsRepository = mockk<NewsRepository> {
            coEvery { getNewsStories } returns fakeNetworkFlow
        }

        val viewModel = MainViewModel(fakeNewsRepository, mockDatabase)

        viewModel.uiState.test {

            assertEquals(NewsState.Loading(true), awaitItem())


            fakeDbFlow.emit(listOf(TopStories("Title1"), TopStories("Title2")))
            assertEquals(NewsState.Loading(false), awaitItem()) // Stop loading
            assertEquals(
                NewsState.LocalSuccess(listOf(TopStories("Title1"), TopStories("Title2"))),
                awaitItem()
            ) // Local success


            fakeNetworkFlow.emit(Result.success(Results(listOf(TopStories("Title3")))))
            assertEquals(
                NewsState.RemoteSuccess(listOf(TopStories("Title3"))),
                awaitItem()
            ) // Remote success
        }
    }

    @Test
    fun `init should emit Error state on failed API call`() = runTest {

        coEvery { mockRepository.getNewsStories } returns fakeNetworkFlow
        coEvery { mockDatabase.topStoriesDao() } returns fakeTopStoryDao
        coEvery { mockDatabase.topStoriesDao().getStories() } returns MutableStateFlow(emptyList())
        coEvery { mockDatabase.topStoriesDao().insertStories(any()) } just Runs

        val viewModel = MainViewModel(mockRepository, mockDatabase)

        viewModel.uiState.test {
            assertEquals(NewsState.Loading(true), awaitItem())
            assertEquals(NewsState.Loading(false), awaitItem())
            assertEquals(NewsState.LocalSuccess(emptyList()), awaitItem())

            fakeNetworkFlow.emit(Result.failure(Exception("Network error")))

            assert(awaitItem() is NewsState.Error)
        }

    }

    @Test
    fun `updateState should emit new state`() = runTest {

        coEvery { mockDatabase.topStoriesDao() } returns fakeTopStoryDao
        coEvery { mockDatabase.topStoriesDao().getStories() } returns MutableStateFlow(emptyList())
        coEvery { mockRepository.getNewsStories } returns fakeNetworkFlow
        val viewModel = MainViewModel(mockRepository, mockDatabase)

        viewModel.uiState.test {

            skipItems(1)

            val newState = NewsState.LocalSuccess(emptyList())
            viewModel.updateState(newState)

            assertEquals(newState, awaitItem())
        }
    }

    @Test
    fun `saveInDb should insert stories into database`() = runTest {
        // Given: Mock DB & DAO
        val mockDao = mockk<TopStoryDao>(relaxed = true)
        val mockDatabase = mockk<NewslyDatabase> {
            every { topStoriesDao() } returns mockDao
        }

        coEvery { mockRepository.getNewsStories } returns fakeNetworkFlow

        val viewModel = MainViewModel(mockRepository, mockDatabase)

        // When: Calling saveInDb with fake data
        val fakeResults = Results(listOf(TopStories("Title1"), TopStories("Title2")))
        viewModel.saveInDb(fakeResults)

        coVerify(exactly = 1) { mockDao.insertStories(fakeResults.results) }
    }
}
