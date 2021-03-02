#!/usr/bin/ksh

echo 'User (Short) Maritime'
java com.amsa.Hexdecode.Hexdecode BEE9031534D34D1 TEXT
java com.amsa.Hexdecode.Hexdecode 65946B219A69A68F63811000000000 TEXT
java com.amsa.Hexdecode.Hexdecode 50F52490DDCC2689B79F4000000000 TEXT
java com.amsa.Hexdecode.Hexdecode 5A34CB219A69AE8846B70000000000 TEXT
java com.amsa.Hexdecode.Hexdecode 59F52499EDF786898C2A0000000000 TEXT
java com.amsa.Hexdecode.Hexdecode 511452052081A68D6C230000000000 TEXT

echo 'User Short Test'
java com.amsa.Hexdecode.Hexdecode 5A3F0E1A59364E8642F68000000000 TEXT

echo 'User (Short) Aviation'
java com.amsa.Hexdecode.Hexdecode 56E30E1A4324920310DBC0FFFFFFFF TEXT

echo 'User (Short) AirCraft Operator'
java com.amsa.Hexdecode.Hexdecode 4E3670D380A9028800000000000000 TEXT

echo 'User (Short) Serial AirCraft Address'
java com.amsa.Hexdecode.Hexdecode 4E86C024645FFFFFFFFFFF00000000 TEXT

echo 'User (Short) Serial Maritime Float Free'
java com.amsa.Hexdecode.Hexdecode 4E86A407E200052A42ECF080000000 TEXT
java com.amsa.Hexdecode.Hexdecode 56E68102A880800BA903C000000000 TEXT
java com.amsa.Hexdecode.Hexdecode 56E6826C1EA020086B404000000000 TEXT
java com.amsa.Hexdecode.Hexdecode 56E6826EE8A0C008A5854000000000 TEXT

echo 'User (Short) Serial Maritime NON Float Fre'
java com.amsa.Hexdecode.Hexdecode 56E7010D68A040086A998000000000 TEXT

echo 'User Location (Long) Serial Maritime Float Free'
java com.amsa.Hexdecode.Hexdecode E0768553AE1D05BFD991FCFFFFFFFF TEXT

echo 'User Location (Long) National User'
java com.amsa.Hexdecode.Hexdecode D6E9C0321FDFE96287BC7FFFFF8AAC TEXT
java com.amsa.Hexdecode.Hexdecode D6E9C0321FDFE6625D2C7FFFFF5DF2 TEXT

echo 'User Location (Long) Radio Callsign'
java com.amsa.Hexdecode.Hexdecode FB5D54C34D347C19A6844CFFFFFFFF TEXT

echo 'Standard Location (Short) Aircraft Address'
java com.amsa.Hexdecode.Hexdecode 1F73BC614E7FDFFED5D77500000000 TEXT

echo 'Standard Location (Short) Serial Epirb'
java com.amsa.Hexdecode.Hexdecode 1F761CC073A3CE72E682B700000000 TEXT

echo 'Standard Location (Short) Serial PLB'
java com.amsa.Hexdecode.Hexdecode 9F771CC3E7A3CE7225DEB7BD566D7E TEXT

echo 'Standard Location (Long) Aircraft Address'
java com.amsa.Hexdecode.Hexdecode 8DB345B146202DDF3C71F59BAB7072 TEXT
java com.amsa.Hexdecode.Hexdecode 96E3ADFC437FDFFBB7EF7583E0FAA8 TEXT
java com.amsa.Hexdecode.Hexdecode 96E3AAAAAA7FDFF8211F3583E0FAA8 TEXT
java com.amsa.Hexdecode.Hexdecode 9F73BC614E7FDFFD2D747583E0FAA8 TEXT
java com.amsa.Hexdecode.Hexdecode 96E3AAAAAA7FDFF8211F35FFFFFFFF TEXT
java com.amsa.Hexdecode.Hexdecode A0234BF8A7335D0AFABB00AD6C4FF9 TEXT
java com.amsa.Hexdecode.Hexdecode A0234BF8A7335D0AFABB0000000000 TEXT

echo 'Standard Location (Long) Test'
java com.amsa.Hexdecode.Hexdecode 96EE1980EB322EF48A39F700000000 TEXT

echo 'Standard Location (Short) ELT - Serial'
java com.amsa.Hexdecode.Hexdecode 10D41A02AA7FDFF800000000000000 TEXT

echo 'Standard Location (Long) ELT - Serial'
java com.amsa.Hexdecode.Hexdecode 9AF41A02D87FDFFE3B64F5FFFFFFFF TEXT
java com.amsa.Hexdecode.Hexdecode AC641A014C7FDFFD1E9D75FFFFFFFF TEXT
java com.amsa.Hexdecode.Hexdecode AC641A014C7FDFFD1E9D7500000000 TEXT
java com.amsa.Hexdecode.Hexdecode 8DA41A02C17FDFF83B4235FFFFFFFF TEXT
java com.amsa.Hexdecode.Hexdecode 8F741A02CA7FDFFB28D635FFFFFFFF TEXT

echo 'Standard Location (Long) Ship MMSI'
java com.amsa.Hexdecode.Hexdecode 8C9201A43A9C8ADF391A4800000000 TEXT

echo 'National Location (Short) Personal PLB'
java com.amsa.Hexdecode.Hexdecode 1F7B00F9E8EC737DABB13700000000 TEXT
java com.amsa.Hexdecode.Hexdecode 1F7B00F9E8EC737DABB137FFFFFFFF TEXT

echo 'National Location (Long) Personal PLB'
java com.amsa.Hexdecode.Hexdecode 8E3B19DEDFC0FF07815F77FFFFFFFF TEXT
java com.amsa.Hexdecode.Hexdecode 9F7B00F9E8EC737E5312378A1802B0 TEXT
java com.amsa.Hexdecode.Hexdecode 8E3B15F1DFC0FF07FD1F769F3C0672 TEXT
java com.amsa.Hexdecode.Hexdecode 9D5BA2E5336E0D67F70EFFFFFFFFFF TEXT

echo 'National Location (Long) Maritime EPIRB'
java com.amsa.Hexdecode.Hexdecode 8E8A00001FC0FF01DA42DB7FFFFFFF TEXT
