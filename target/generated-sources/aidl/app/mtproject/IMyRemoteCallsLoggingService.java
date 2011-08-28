/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\mtproject\\src\\app\\mtproject\\IMyRemoteCallsLoggingService.aidl
 */
package app.mtproject;
public interface IMyRemoteCallsLoggingService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements app.mtproject.IMyRemoteCallsLoggingService
{
private static final java.lang.String DESCRIPTOR = "app.mtproject.IMyRemoteCallsLoggingService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an app.mtproject.IMyRemoteCallsLoggingService interface,
 * generating a proxy if needed.
 */
public static app.mtproject.IMyRemoteCallsLoggingService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof app.mtproject.IMyRemoteCallsLoggingService))) {
return ((app.mtproject.IMyRemoteCallsLoggingService)iin);
}
return new app.mtproject.IMyRemoteCallsLoggingService.Stub.Proxy(obj);
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
case TRANSACTION_dumpCallsLog:
{
data.enforceInterface(DESCRIPTOR);
this.dumpCallsLog();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements app.mtproject.IMyRemoteCallsLoggingService
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
public void dumpCallsLog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_dumpCallsLog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_dumpCallsLog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void dumpCallsLog() throws android.os.RemoteException;
}
