package interface_adapter;

/**
 * The View Model for the RecipeListView.
 */
public class RecipeListViewModel extends ViewModel<RecipeListState> {

    public RecipeListViewModel() {
        // TODO
        super("bookmarks");
        setState(new RecipeListState());
    }

}
