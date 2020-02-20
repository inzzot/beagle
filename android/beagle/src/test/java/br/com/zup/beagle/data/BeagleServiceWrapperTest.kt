package br.com.zup.beagle.data

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.data.serializer.BeagleSerializer
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private val URL = RandomData.httpUrl()

@ExperimentalCoroutinesApi
class BeagleServiceWrapperTest {

    @MockK
    private lateinit var beagleService: BeagleService

    @MockK
    private lateinit var beagleSerializer: BeagleSerializer

    @MockK
    private lateinit var component: ServerDrivenComponent

    @MockK
    private lateinit var throwable: Throwable

    @MockK
    private lateinit var listener: FetchListener

    private lateinit var subject: BeagleServiceWrapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk } returns mockk(relaxed = true)

        subject = BeagleServiceWrapper()
    }

    @After
    fun tearDown() {
        unmockkObject(BeagleEnvironment)

        Dispatchers.resetMain()
    }

    @Test
    fun fetch_when_success_should_call_onSuccess() = runBlockingTest {
        // GIVEN
        subject.init(beagleService,beagleSerializer)
        coEvery { beagleService.fetchComponent(URL) } returns component
        every { listener.onSuccess(any()) } just Runs

        // WHEN
        subject.fetchComponent(URL, listener)

        // THEN
        verify(exactly = once()) { listener.onSuccess(component) }
    }

    @Test
    fun fetch_when_fail_should_call_onError() = runBlockingTest {
        // GIVEN
        subject.init(beagleService,beagleSerializer)
        coEvery { beagleService.fetchComponent(URL) } throws throwable
        every { listener.onError(any()) } just Runs

        // WHEN
        subject.fetchComponent(URL, listener)

        // THEN
        verify(exactly = once()) { listener.onError(throwable) }
    }

    @Test
    fun deserializeWidget_when_call_method() = runBlockingTest {
        // GIVEN
        subject.init(beagleService,beagleSerializer)
        val randomString = RandomData.string()
        every { beagleSerializer.deserializeComponent(randomString) } returns component

        // WHEN
        subject.deserializeComponent(randomString)

        // THEN
        assertEquals(beagleSerializer.deserializeComponent(randomString), component)
    }

    @Test
    fun serializeWidget_when_call_method() = runBlockingTest {
        // GIVEN
        subject.init(beagleService,beagleSerializer)
        val randomString = RandomData.string()
        every { beagleSerializer.serializeComponent(component) } returns randomString

        // WHEN
        subject.serializeComponent(component)

        // THEN
        assertEquals(beagleSerializer.serializeComponent(component), randomString)
    }
}