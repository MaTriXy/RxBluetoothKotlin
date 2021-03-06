package com.vincentmasselis.rxbluetoothkotlin

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.vincentmasselis.rxbluetoothkotlin.CannotInitialize.CannotInitializeDescriptorReading
import com.vincentmasselis.rxbluetoothkotlin.CannotInitialize.CannotInitializeDescriptorWrite
import com.vincentmasselis.rxbluetoothkotlin.DeviceDisconnected.DescriptorReadDeviceDisconnected
import com.vincentmasselis.rxbluetoothkotlin.DeviceDisconnected.DescriptorWriteDeviceDisconnected
import com.vincentmasselis.rxbluetoothkotlin.IOFailed.DescriptorReadingFailed
import com.vincentmasselis.rxbluetoothkotlin.IOFailed.DescriptorWriteFailed
import com.vincentmasselis.rxbluetoothkotlin.internal.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

fun BluetoothGatt.rxRead(descriptor: BluetoothGattDescriptor): Maybe<ByteArray> =
        EnqueueSingle(semaphore, assertConnected { device, reason -> DescriptorReadDeviceDisconnected(device, reason, descriptor.characteristic.service, descriptor.characteristic, descriptor) }) {
            Single
                    .create<Pair<BluetoothGattDescriptor, Int>> { downStream ->
                        downStream.setDisposable(descriptorReadSubject.firstOrError().subscribe({ downStream.onSuccess(it) }, { downStream.tryOnError(it) }))
                        logger?.v(TAG, "readDescriptor ${descriptor.uuid}")
                        if (readDescriptor(descriptor).not())
                            downStream.tryOnError(CannotInitializeDescriptorReading(
                                    device,
                                    descriptor.characteristic?.service,
                                    descriptor.characteristic,
                                    descriptor,
                                    internalService(),
                                    clientIf(),
                                    descriptor.characteristic?.service?.device(),
                                    isDeviceBusy()))
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
        }
                .flatMap { (readDescriptor, status) ->
                    if (status != BluetoothGatt.GATT_SUCCESS) Maybe.error(DescriptorReadingFailed(status, device, readDescriptor.characteristic.service, readDescriptor.characteristic, readDescriptor))
                    else Maybe.just(readDescriptor.value)
                }

fun BluetoothGatt.rxWrite(descriptor: BluetoothGattDescriptor, value: ByteArray, checkIfAlreadyWritten: Boolean = false): Completable =
        EnqueueSingle(semaphore, assertConnected { device, reason -> DescriptorWriteDeviceDisconnected(device, reason, descriptor.characteristic.service, descriptor.characteristic, descriptor, value) }) {
            Single
                    .create<Pair<BluetoothGattDescriptor, Int>> { downStream ->
                        if (checkIfAlreadyWritten && Arrays.equals(descriptor.value, value)) {
                            downStream.onSuccess(descriptor to BluetoothGatt.GATT_SUCCESS)
                            return@create
                        }

                        downStream.setDisposable(descriptorWriteSubject.firstOrError().subscribe({ downStream.onSuccess(it) }, { downStream.tryOnError(it) }))
                        logger?.v(TAG, "writeDescriptor ${descriptor.uuid} with value ${value.toHexString()}")
                        descriptor.value = value
                        if (writeDescriptor(descriptor).not())
                            downStream.tryOnError(CannotInitializeDescriptorWrite(
                                    device,
                                    descriptor.characteristic?.service,
                                    descriptor.characteristic,
                                    descriptor,
                                    value,
                                    internalService(),
                                    clientIf(),
                                    descriptor.characteristic?.service?.device(),
                                    isDeviceBusy()))
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
        }
                .flatMapCompletable { (wroteDescriptor, status) ->
                    if (status != BluetoothGatt.GATT_SUCCESS) Completable.error(DescriptorWriteFailed(status, device, wroteDescriptor.characteristic.service, wroteDescriptor.characteristic, wroteDescriptor, value))
                    else Completable.complete()
                }