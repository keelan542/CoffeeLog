import android.arch.persistence.db.SupportSQLiteDatabase;
import android.support.annotation.NonNull;

public class CoffeeMigration extends android.arch.persistence.room.migration.Migration {
    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion   The end version of the database after this migration is applied.
     */
    public CoffeeMigration(int startVersion, int endVersion) {
        super(startVersion, endVersion);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        // Table not altered, so no more code needed here
    }
}
