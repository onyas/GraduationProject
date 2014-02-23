package com.onyas.phoneguard.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.onyas.phoneguard.domain.ContactInfo;
import com.onyas.phoneguard.engine.ContactEngine;

public class TestContactEngine extends AndroidTestCase {

	public void testGetContacts() {
		ContactEngine contactEngine = new ContactEngine(getContext());
		List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		contacts = contactEngine.getContacts();
		for (ContactInfo contact : contacts) {
			System.out.println(contact.getName() + "---" + contact.getNumber());
		}
	}

}
