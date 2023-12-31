package pa.chan.hammersystemtest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pa.chan.hammersystemtest.data.userException.ConnectionException
import pa.chan.hammersystemtest.data.userException.UserError
import pa.chan.hammersystemtest.domain.GetCategoriesUseCase
import pa.chan.hammersystemtest.domain.GetDishesUseCase
import pa.chan.hammersystemtest.domain.model.DishModel
import javax.inject.Inject


@HiltViewModel
class DishesViewModel @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _dishesLiveData: MutableLiveData<List<DishModel>> = MutableLiveData()
    val dishesLiveData: LiveData<List<DishModel>>
        get() = _dishesLiveData

    private val _categoryLiveData: MutableLiveData<Set<String>> = MutableLiveData()
    val categoryLiveData: LiveData<Set<String>>
        get() = _categoryLiveData

    private val _errorLiveData: MutableLiveData<UserError> = MutableLiveData()
    val errorLiveData: LiveData<UserError>
        get() = _errorLiveData

    fun fetchDishes() {
        viewModelScope.launch {

            try {
                val dishes = getDishesUseCase()
                val categories = getCategoriesUseCase(dishes)
                _dishesLiveData.postValue(dishes)
                _categoryLiveData.postValue(categories)
            } catch (e: ConnectionException) {
                _errorLiveData.postValue(e)
            }

        }
    }


}