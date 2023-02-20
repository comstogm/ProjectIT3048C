import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.projectit3048c.ss23.MainActivity


val appModule = module {
    viewModel { MainViewModel() }
}