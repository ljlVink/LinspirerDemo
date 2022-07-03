package com.ljlVink.core.t11_271bay;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IUidSystemService extends IInterface {
    boolean InnerClearCameraDouble() throws RemoteException;

    boolean InnerColorDisplayIsActivated() throws RemoteException;

    void InnerColorDisplaySetActivated(boolean z) throws RemoteException;

    boolean InnerDebugMode(boolean z) throws RemoteException;

    boolean InnerDisablePackage(String str, boolean z) throws RemoteException;

    void InnerDisableSdCard() throws RemoteException;

    void InnerDisableStatusBarAndHomeRecent() throws RemoteException;

    void InnerDisableUsbState() throws RemoteException;

    void InnerEnableSdCard() throws RemoteException;

    void InnerEnableStatusBarAndHomeRecent() throws RemoteException;

    void InnerEnableUsbState() throws RemoteException;

    boolean InnerFactoryReset() throws RemoteException;

    boolean InnerForceStopPackage(String str) throws RemoteException;

    boolean InnerLocationMode(boolean z) throws RemoteException;

    void InnerNavBarBack(boolean z) throws RemoteException;

    void InnerNavBarHome(boolean z) throws RemoteException;

    void InnerNavBarManager(int i) throws RemoteException;

    void InnerNavBarRecent(boolean z) throws RemoteException;

    boolean InnerReboot() throws RemoteException;

    String InnerScreenCap() throws RemoteException;

    boolean InnerSetClockTime(long j) throws RemoteException;

    boolean InnerSetTimeAuto(boolean z) throws RemoteException;

    boolean InnerSettingsChange(String str, String str2, int i) throws RemoteException;

    boolean InnerUnknownApps(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IUidSystemService {
        public static IUidSystemService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.tensafe.app.onerun.IUidSystemService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IUidSystemService)) {
                return new Proxy(iBinder);
            }
            return (IUidSystemService) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                boolean z = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerReboot = InnerReboot();
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerReboot ? 1 : 0);
                        return true;
                    case 2:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerForceStopPackage = InnerForceStopPackage(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerForceStopPackage ? 1 : 0);
                        return true;
                    case 3:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        boolean InnerDebugMode = InnerDebugMode(z);
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerDebugMode ? 1 : 0);
                        return true;
                    case 4:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        boolean InnerLocationMode = InnerLocationMode(z);
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerLocationMode ? 1 : 0);
                        return true;
                    case 5:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        boolean InnerUnknownApps = InnerUnknownApps(z);
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerUnknownApps ? 1 : 0);
                        return true;
                    case 6:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerClearCameraDouble = InnerClearCameraDouble();
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerClearCameraDouble ? 1 : 0);
                        return true;
                    case 7:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        String readString = parcel.readString();
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        boolean InnerDisablePackage = InnerDisablePackage(readString, z);
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerDisablePackage ? 1 : 0);
                        return true;
                    case 8:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        boolean InnerSetTimeAuto = InnerSetTimeAuto(z);
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerSetTimeAuto ? 1 : 0);
                        return true;
                    case 9:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerSettingsChange = InnerSettingsChange(parcel.readString(), parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerSettingsChange ? 1 : 0);
                        return true;
                    case 10:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        String InnerScreenCap = InnerScreenCap();
                        parcel2.writeNoException();
                        parcel2.writeString(InnerScreenCap);
                        return true;
                    case 11:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerColorDisplayIsActivated = InnerColorDisplayIsActivated();
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerColorDisplayIsActivated ? 1 : 0);
                        return true;
                    case 12:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        InnerColorDisplaySetActivated(z);
                        parcel2.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerSetClockTime = InnerSetClockTime(parcel.readLong());
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerSetClockTime ? 1 : 0);
                        return true;
                    case 14:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        boolean InnerFactoryReset = InnerFactoryReset();
                        parcel2.writeNoException();
                        parcel2.writeInt(InnerFactoryReset ? 1 : 0);
                        return true;
                    case 15:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerDisableStatusBarAndHomeRecent();
                        parcel2.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerEnableStatusBarAndHomeRecent();
                        parcel2.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerDisableSdCard();
                        parcel2.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerEnableSdCard();
                        parcel2.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerEnableUsbState();
                        parcel2.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerDisableUsbState();
                        parcel2.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        InnerNavBarManager(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        InnerNavBarHome(z);
                        parcel2.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        InnerNavBarBack(z);
                        parcel2.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface("com.tensafe.app.onerun.IUidSystemService");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        InnerNavBarRecent(z);
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("com.tensafe.app.onerun.IUidSystemService");
                return true;
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IUidSystemService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerReboot() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z = false;
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerForceStopPackage(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeString(str);
                    boolean z = false;
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerDebugMode(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z2 = false;
                    }
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerLocationMode(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z2 = false;
                    }
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerUnknownApps(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z2 = false;
                    }
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerClearCameraDouble() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z = false;
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerDisablePackage(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeString(str);
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z2 = false;
                    }
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerSetTimeAuto(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() == 0) {
                        z2 = false;
                    }
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerSettingsChange(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    boolean z = false;
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public String InnerScreenCap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerColorDisplayIsActivated() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z = false;
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerColorDisplaySetActivated(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerSetClockTime(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeLong(j);
                    boolean z = false;
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public boolean InnerFactoryReset() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    boolean z = false;
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerDisableStatusBarAndHomeRecent() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerEnableStatusBarAndHomeRecent() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerDisableSdCard() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerEnableSdCard() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerEnableUsbState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerDisableUsbState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerNavBarManager(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerNavBarHome(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerNavBarBack(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.tensafe.app.onerun.IUidSystemService
            public void InnerNavBarRecent(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.tensafe.app.onerun.IUidSystemService");
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}