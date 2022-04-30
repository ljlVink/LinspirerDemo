package activitylauncher;

import android.content.Context;

public class AllTasksListAsyncProvider extends AsyncProvider<AllTasksListAdapter> {
    private final AllTasksListAdapter adapter;

    AllTasksListAsyncProvider(
            Context context,
            activitylauncher.AsyncProvider.Listener<AllTasksListAdapter> listener) {
        super(context, listener, true);
        this.adapter = new AllTasksListAdapter(context);
    }

    @Override
    protected AllTasksListAdapter run(Updater updater) {
        this.adapter.resolve(updater);
        return this.adapter;
    }
}
