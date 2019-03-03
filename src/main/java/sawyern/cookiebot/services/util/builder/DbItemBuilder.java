package sawyern.cookiebot.services.util.builder;

import sawyern.cookiebot.models.constants.Constants;
import sawyern.cookiebot.models.entity.DbItem;

import java.util.Date;

public final class DbItemBuilder extends DbItem {
    private DbItemBuilder() {
    }

    public static DbItemBuilder aDbItem() {
        return new DbItemBuilder();
    }

    public DbItem build(DbItem dbItem, String createUser) {
        dbItem.setRevision(1);
        dbItem.setLastRevision(Constants.LATEST_REVISION);
        dbItem.setStatus(Constants.STATUS_ACTIVE);
        dbItem.setCreateDate(new Date());
        dbItem.setCreateUser(createUser);
        dbItem.setModifyDate(new Date());
        dbItem.setModifyUser(createUser);
        return dbItem;
    }
}
