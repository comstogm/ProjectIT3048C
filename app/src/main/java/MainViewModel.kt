import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dto.Food
import kotlinx.coroutines.launch
import service.FoodService

class MainViewModel(var foodService : FoodService =  FoodService()) : ViewModel() {

    var foods : MutableLiveData<Food?> = MutableLiveData<Food?>()


    fun fetchFoods() {
        viewModelScope.launch {
            var innerFoods = foodService.fetchFoods()
            foods.postValue(innerFoods)
        }

    }
}