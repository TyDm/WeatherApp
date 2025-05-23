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

class AddCityUseCaseTest {
    
    private lateinit var repository: WeatherRepository
    private lateinit var useCase: AddCityUseCase
    
    @Before
    fun setup() {
        repository = mockk()
        useCase = AddCityUseCase(repository)
    }
    
    @Test
    fun `invoke returns success when repository succeeds`() = runTest {
        // Given
        val locationKey = "123456"
        val language = "ru"
        coEvery { 
            repository.addCity(locationKey, language) 
        } returns WeatherResult.Success(Unit)
        
        // When
        val result = useCase(locationKey, language)
        
        // Then
        assertThat(result).isInstanceOf(WeatherResult.Success::class.java)
    }
    
    @Test
    fun `invoke returns error when repository fails`() = runTest {
        // Given
        val locationKey = "123456"
        val language = "ru"
        val error = WeatherError.NetworkError(Exception("Network error"))
        coEvery { 
            repository.addCity(locationKey, language) 
        } returns WeatherResult.Error(error)
        
        // When
        val result = useCase(locationKey, language)
        
        // Then
        assertThat(result).isInstanceOf(WeatherResult.Error::class.java)
        assertThat((result as WeatherResult.Error).error)
            .isInstanceOf(WeatherError.NetworkError::class.java)
    }
} 