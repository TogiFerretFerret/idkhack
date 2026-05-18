package sh.idktheflag.idk.api.management.accounts;

import sh.idktheflag.idk.api.config.ISavable;
import sh.idktheflag.idk.api.management.accounts.types.CrackedAccount;
import sh.idktheflag.idk.api.management.accounts.types.MicrosoftAccount;

import java.util.*;

public class Accounts implements ISavable, Iterable<Account<?>> {
    public static Accounts INSTANCE;
    private final List<Account<?>> accounts = new ArrayList<>();

    public Accounts() {
        INSTANCE = this;
    }

    public void add(Account<?> account) {
        accounts.add(account);
        saveConfig();
    }

    public void remove(Account<?> account) {
        accounts.remove(account);
        saveConfig();
    }

    private void saveConfig() {
        try {
            if (sh.idktheflag.idk.api.management.SavableManager.INSTANCE != null) {
                sh.idktheflag.idk.api.management.SavableManager.INSTANCE.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return accounts.size();
    }

    @Override
    public Iterator<Account<?>> iterator() {
        return accounts.iterator();
    }

    @Override
    public void load(Map<String, Object> objects) {
        if (objects == null) return;
        List<Map<String, Object>> list = (List<Map<String, Object>>) objects.get("accounts");
        if (list == null) return;

        accounts.clear();
        for (Map<String, Object> map : list) {
            try {
                AccountType type = AccountType.valueOf((String) map.get("type"));
                Account<?> account = switch (type) {
                    case Cracked -> new CrackedAccount("").load(map);
                    case Microsoft -> new MicrosoftAccount("").load(map);
                    default -> null;
                };
                if (account != null) accounts.add(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> save() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Account<?> account : accounts) {
            list.add(account.save());
        }
        map.put("accounts", list);
        return map;
    }

    @Override
    public String getFileName() {
        return "accounts.yml";
    }

    @Override
    public String getDirName() {
        return "accounts";
    }
}
