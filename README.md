# FindMySong
Simple application that let you find songs in iTunes Search API and/or local database (JSON file).

## Few words on the architecture
App implements Clean Architecture. Communication between *Controller* and *View* is done via observable variables on the other hand, view is able to call *Controller* methods directly. All data manipulations are done in *Interactors* that are injected into *Controllers*. Data is provided by the *Repository*. Every app layer uses different models. Mapping between them is done by *Mapper*.

## App is written using
- Dagger2
- AAC (ViewModel, LiveData)
- RxJava2
- Retrofit2
- Mockito2 (testing)