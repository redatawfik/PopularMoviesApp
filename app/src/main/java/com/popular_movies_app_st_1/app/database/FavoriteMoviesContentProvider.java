package com.popular_movies_app_st_1.app.database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.popular_movies_app_st_1.app.database.FavoriteMoviesContract.FavoriteMoviesEntry;


import static com.popular_movies_app_st_1.app.database.FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;

public class FavoriteMoviesContentProvider extends ContentProvider {

    private static final int TASKS = 100;
    private static final int TASK_WITH_ID = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, TASKS);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", TASK_WITH_ID);

        return uriMatcher;
    }


    private FavoriteMoviesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();


        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case TASKS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Add a case to query for a single row of data by ID
            // Use selections and selectionArgs to filter for that ID
            case TASK_WITH_ID:
                // Get the id from the URI
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = FavoriteMoviesEntry.COLUMN_MOVIE_Id + " =?";
                String[] mSelectionArgs = new String[]{id};

                // Construct a query as we would normally, passing in the selection/args
                retCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case TASKS:
                // Insert new values into the database
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int movieDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            case TASKS:
                movieDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                // Handle the single item case, recognized by the ID included in the URI path
                break;
            case TASK_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = FavoriteMoviesEntry.COLUMN_MOVIE_Id + " =?";
                String[] mSelectionArgs = new String[]{id};

                // Use selections/selectionArgs to filter for this ID
                movieDeleted = db.delete(TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change
        if (movieDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //Keep track of if an update occurs
        int tasksUpdated;

        // match code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case TASK_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = FavoriteMoviesEntry.COLUMN_MOVIE_Id + "=?";
                String[] mSelectionArgs = new String[]{id};

                tasksUpdated = mDbHelper.getWritableDatabase().update(TABLE_NAME,
                        values,
                        mSelection,
                        mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return number of tasks updated
        return tasksUpdated;
    }
}
