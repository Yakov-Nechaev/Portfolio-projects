package com.yakov.cinema.data.repository

import com.yakov.cinema.constants_and_extensions.mapToBriefMovie
import com.yakov.cinema.constants_and_extensions.mapToBriefMovieList
import com.yakov.cinema.constants_and_extensions.mapToCrewMovie
import com.yakov.cinema.constants_and_extensions.mapToDetailsMovie
import com.yakov.cinema.constants_and_extensions.mapToListImagesMovie
import com.yakov.cinema.constants_and_extensions.mapToPersonnelDetails
import com.yakov.cinema.data.model.app_model.AllIdMovie
import com.yakov.cinema.data.model.app_model.BriefMovie
import com.yakov.cinema.data.model.app_model.CollectionMovie
import com.yakov.cinema.data.model.app_model.Crew
import com.yakov.cinema.data.model.app_model.DetailsMovie
import com.yakov.cinema.data.model.app_model.ImagesMovie
import com.yakov.cinema.data.model.app_model.PersonnelDetails
import com.yakov.cinema.data.model.app_model.SearchParams
import com.yakov.cinema.data.network.NetworkService
import com.yakov.cinema.data.store.StoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(var storeManager: StoreManager) {

    suspend fun getDetailsMovieFromCollection(name: String): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                val listId = storeManager.getMovieListFromCollection(name)
                val briefMovieList = listId.map {
                    NetworkService.cinemaService.getMovieById(it).mapToBriefMovie()
                }
                briefMovieList
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun deleteCollection(name: String) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.deleteItemCollection(name)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun addCollection(name: String) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.addCollection(name)

            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getWelcomeByStore() : Boolean? {
        return withContext(Dispatchers.IO) {
            storeManager.getWelcome()
        }
    }

    suspend fun saveWithWelcome() {
        withContext(Dispatchers.IO) {
            storeManager.withWelcome()
        }
    }

    suspend fun saveWithoutWelcome() {
        withContext(Dispatchers.IO) {
            storeManager.withoutWelcome()
        }
    }

    suspend fun getCollectionListMovie(name: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                storeManager.getMovieListFromCollection(name)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun addMovieInCollection(name: String, id: Int) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.addItemMovieInCollection(name, id)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun removeMovieFromCollection(name: String, id: Int) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.removeItemMovieFromCollection(name, id)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getCollections(): List<CollectionMovie> {
        return withContext(Dispatchers.IO) {
            try {
                storeManager.getCollectionsList().map {
                    val collectionMovieId =
                        storeManager.getMovieListFromCollection(it).map { id -> AllIdMovie(id.toInt()) }
                    CollectionMovie(name = it, collectionMovieId)
                }
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun fillInitialCollections() {
        withContext(Dispatchers.IO) {
            try {
                storeManager.setDefaultCollection()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getListMovieWasInterested(): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                val listId = storeManager.getWasInterested()
                val listMovie = listId.map {
                    NetworkService.cinemaService.getMovieById(it).mapToBriefMovie()

                }
                listMovie
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getListMovieWasViewed(): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                val listId = storeManager.getWasViewed()
                val listMovie = listId.map {
                    NetworkService.cinemaService.getMovieById(it).mapToBriefMovie()
                }
                listMovie
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun clearAllViewed() {
        withContext(Dispatchers.IO) {
            try {
                storeManager.clearAllWasViewed()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getListViewedId(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                storeManager.getWasViewed()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun listViewedAddItem(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.saveWasViewed(id)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun listViewedRemoveItem(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.deleteItemWasViewed(id)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun clearWasInterested() {
        withContext(Dispatchers.IO) {
            try {
                storeManager.clearWasInterested()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun saveInWasInterested(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                storeManager.saveWasInterested(id)
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getListBySearch(searchParams: SearchParams): MutableList<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getSearch(
                    countries = searchParams.countries,
                    genres = searchParams.genres,
                    type = searchParams.type,
                    order = searchParams.order,
                    ratingFrom = searchParams.ratingFrom,
                    ratingTo = searchParams.ratingTo,
                    yearFrom = searchParams.yearFrom,
                    yearTo = searchParams.yearTo,
                    keyword = searchParams.keyword,
                )?.mapToBriefMovieList()?.toSet()?.toMutableList() ?: mutableListOf()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getPersonnelDetailsData(id: Int): PersonnelDetails {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getPersonnelDetails(id.toString())
                    .mapToPersonnelDetails()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }


    suspend fun getPopularListFull(page: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.popular(page = page).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getDramFranceFull(page: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getDramaFrance(page = page).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getSoapFull(page: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getSeries(page = page).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }


    suspend fun getPremieres(year: Int, month: String, page: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getPremieres(year, month, page).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getActionUSAFull(page: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getActionUSA(page = page).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getTopListFull(page: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getTop(page = page).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getMovie(id: Int): DetailsMovie {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getMovieById(id.toString()).mapToDetailsMovie()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getTeam(id: Int): Crew {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.actorsList(id).mapToCrewMovie()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getSimilarMovie(id: Int): List<BriefMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getSameMovie(id.toString()).mapToBriefMovieList()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }

    suspend fun getImagesShort(id: Int): List<ImagesMovie> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkService.cinemaService.getImagesShort(id.toString()).mapToListImagesMovie()
            } catch (e: Exception) {
                throw (e)
            }
        }
    }
}