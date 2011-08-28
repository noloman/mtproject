/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\mtproject\\src\\app\\mtproject\\IMyRemoteLocationLoggingService.aidl
 */
package app.mtproject;
public interface IMyRemoteLocationLoggingService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements app.mtproject.IMyRemoteLocationLoggingService
{
private static final java.lang.String DESCRIPTOR = "app.mtproject.IMyRemoteLocationLoggingService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an app.mtproject.IMyRemoteLocationLoggingService interface,
 * generating a proxy if needed.
 */
public static app.mtproject.IMyRemoteLocationLoggingService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof app.mtproject.IMyRemoteLocationLoggingService))) {
return ((app.mtproject.IMyRemoteLocationLoggingService)iin);
}
return new app.mtproject.IMyRemoteLocationLoggingService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_dumpLocationLog:
{
data.enforceInterface(DESCRIPTOR);
this.dumpLocationLog();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements app.mtproject.IMyRemoteLocationLoggingService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void dumpLocationLog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_dumpLocationLog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_dumpLocationLog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void dumpLocationLog() throws android.os.RemoteException;
}
