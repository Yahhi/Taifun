package ru.develop_for_android.taifun;

import ru.develop_for_android.taifun_data.AddressEntry;

public interface AddressChangesListener {
    void deleteAddress(AddressEntry address);
    void makeAddressDefault(int addressId);
    void editAddress(AddressEntry addressEntry);
}
