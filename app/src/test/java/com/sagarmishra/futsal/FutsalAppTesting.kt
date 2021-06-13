package com.sagarmishra.futsal

import com.sagarmishra.futsal.Repository.*
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.entityapi.FutsalInstances
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class FutsalAppTesting {
    @Test
    fun loginCheck()
    {
        runBlocking {
            val expectedResult = true
            val repo = UserRepository()
            val response = repo.authenticateUser("sagarmsra","123456")
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    @Test
    fun signupCheck()
    {
        runBlocking {
            val expectedResult = true
            val repo = UserRepository()
            val user = AuthUser(firstName = "Samir",lastName = "Mishra",userName = "samirmsra",password = "123456",confirmPassword = "123456",email="samirmsra10@gmail.com",address = "Swyambhu",gender="Male",dob="2005-04-02",mobileNumber = "9801234567")
            val response = repo.registerUser(user)
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }
    //need to add futsal instance id
    @Test
    fun bookingFutsal()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            val bookingRepository = BookingRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val booking = Booking(mobileNumber = "9802345635",teamMate_mobileNumber = "1234567890",futsalInstance_id = FutsalInstances(_id="607a4f9279e99128a89cff42"))
            val response = bookingRepository.bookFutsal(booking)
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }


    @Test
    fun bookingRecordsRetrieval()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            val bookingRepository = BookingRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val response = bookingRepository.retrieveBookings()
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

   //need to add booking id
    @Test
    fun retrieveSingleRecord()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            val bookingRepository = BookingRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val response = bookingRepository.retrieveSingleRecord("607a7d070268fe31902bb63f")
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    //need to add booking id
    @Test
    fun deleteBooking()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            val bookingRepository = BookingRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val response = bookingRepository.deleteBooking("607a7d070268fe31902bb63f")
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }


    @Test
    fun showFutsals()
    {
        runBlocking {
            val expectedResult = true
            val futsalRepository = FutsalRepository()
            val response = futsalRepository.showFutsals()
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    //add day,date and futsalId
//    @Test
//    fun retrieveTimeInstances()
//    {
//        runBlocking {
//            val expectedResult = true
//            val futsalRepository = FutsalRepository()
//            val response = futsalRepository.retrieveBookingInstance(id="604af29520d9282db0ff5152",day="Saturday",date="2021-03-13")
//            val actualResult = response.success
//            Assert.assertEquals(expectedResult,actualResult)
//        }
//    }

    //add futsalId and rating
    @Test
    fun rateFutsal()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            val futsalRepository = FutsalRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val response = futsalRepository.rateFutsal(id="607a4f9279e99128a89cff3d",rating=4F)
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    @Test
    fun inviteUser()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            val adsRepository = AdsRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val response = adsRepository.invite("sagarmishra455.com@gmail.com")
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    @Test
    fun changePassword()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","1234567").token
            val response = userRepository.changePassword("1234567","123456","123456")
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    @Test
    fun changeDetails()
    {
        runBlocking {
            val expectedResult = true
            val userRepository = UserRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val auth = AuthUser(firstName = "Sagar",lastName = "San",userName = "sagarmsra",gender = "Male",mobileNumber = "9803609163",dob = "1999-12-13",address = "Swyambhu",email="sagarcrcoc@gmail.com")
            val response = userRepository.updateDetails(auth)
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }

    @Test
    fun commentAdd()
    {
        runBlocking {
            val expectedResult = true
            val commentRepository = CommentRepository()
            var userRepository = UserRepository()
            RetrofitService.token = "Bearer "+userRepository.authenticateUser("sagarmsra","123456").token
            val response = commentRepository.addComment("607a4f9279e99128a89cff3d","Awesome futsal!!")
            val actualResult = response.success
            Assert.assertEquals(expectedResult,actualResult)
        }
    }
}