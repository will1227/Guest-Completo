package com.example.guest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.guest.data.Guest
import com.example.guest.data.GuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GuestViewModel(
    private val repository: GuestRepository
) : ViewModel() {

    private val _guests = MutableStateFlow<List<Guest>>(emptyList())
    val guests: StateFlow<List<Guest>> = _guests.asStateFlow()

    init {
        loadGuests()
    }

    fun loadGuests() {
        val loadGuests = repository.getAllGuests()
        _guests.value = loadGuests
    }

    fun insertGuest(guest: Guest) {
        repository.insertGuest(guest)
        loadGuests()
    }
    fun updateGuest(guest: Guest) {
        repository.updateGuest(guest)
        loadGuests()
    }
    fun deleteGuest(guest:Guest) {
        repository.deleteGuest(guest.id)
        loadGuests()
    }
    fun confirmGuest(guest: Guest, confirmed: Boolean) {
            repository.confirmGuest(guest.id, confirmed)
            loadGuests()
        }
    }
