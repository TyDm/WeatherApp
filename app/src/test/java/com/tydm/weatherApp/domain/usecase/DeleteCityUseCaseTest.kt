package com.tydm.weatherApp.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tydm.weatherApp.domain.model.WeatherError
import com.tydm.weatherApp.domain.model.WeatherResult
import com.tydm.weatherApp.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DeleteCityUseCaseTest {
    private lateinit var useCase: DeleteCityUseCase
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = DeleteCityUseCase(repository)
    }

    @Test
    fun `delete city`() = runTest {
        val id = 1
        coEvery { repository.deleteCity(id) } returns WeatherResult.Success(Unit)

        val result = useCase(id)
        assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
    }

    @Test
    fun `delete city error`() = runTest {
        val id = 1
        coEvery { repository.deleteCity(id) } returns WeatherResult.Error(WeatherError.DatabaseError(IOException()))

        val result = useCase(id)
        assertThat(result).isInstanceOf(WeatherResult.Error::class.java)
        assertThat((result as WeatherResult.Error).error)
            .isInstanceOf(WeatherError.DatabaseError::class.java)
    }

}