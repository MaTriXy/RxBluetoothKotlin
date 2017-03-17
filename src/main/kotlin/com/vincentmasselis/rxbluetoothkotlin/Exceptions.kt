package com.vincentmasselis.rxbluetoothkotlin

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import java.util.*


// ------------------ Bluetooth exceptions for scanning and I/O

/**
 * Fire this error if the bluetooth is turned off on the current device. You can ask the user to
 * enable Bluetooth by starting an activity for result with this intent :
 * [android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE]
 */
class BluetoothIsTurnedOff : Throwable()

// ------------------ Bluetooth exceptions for scanning only

/**
 * Fire this error if the current device doesn't support Bluetooth.
 */
class DeviceDoesNotSupportBluetooth : Throwable()

/**
 * Error fired if the location permission is require for the current app. You have to request for
 * the missing permission [android.Manifest.permission.ACCESS_COARSE_LOCATION] or
 * [android.Manifest.permission.ACCESS_FINE_LOCATION]
 */
class NeedLocationPermission : Throwable()

/**
 * Fired if location service is disabled for the app. You can ask the user to enable Location
 * service by starting an activity for result with this intent :
 * [android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS]
 */
class LocationServiceDisabled : Throwable()

/**
 * Fired if an error append when scanning.
 * The [reason] field is filled with an [Int] provided by the Android framework, it could be one of
 * the [android.bluetooth.le.ScanCallback.SCAN_FAILED_*] const
 */
class ScanFailedException(val reason: Int) : Throwable() {
    override fun toString(): String {
        return "ScanFailedException(reason=$reason)"
    }
}


// ------------------ Bluetooth exceptions when connected

/**
 * Fire this error if the connect() method returns false when attempting to connect to the device
 */
class CannotInitializeConnection(val bluetoothDevice: BluetoothDevice) : Throwable() {
    override fun toString(): String {
        return "CannotInitializeConnection(bluetoothDevice=$bluetoothDevice)"
    }
}

/**
 * Fire this error if the connection to the device take most time than the specified timeout values.
 */
//TODO, remove this throwable
class GattConnectionTimeout(val bluetoothDevice: BluetoothDevice) : Throwable() {
    override fun toString(): String {
        return "GattConnectionTimeout(bluetoothDevice=$bluetoothDevice)"
    }
}

/**
 * Fired when a I/O operation takes more than 6 seconds to be executed. Normally, a
 * [DeviceDisconnected] exception should be fired before this exception because the bluetooth
 * standard define a Timeout of 3 seconds but, sometimes, the Android BLE framework doesn't call the
 * callback and the [DeviceDisconnected] exception is not fired ¯\_(ツ)_/¯. Is this case, this
 * exception is fired.
 */
class BluetoothTimeout : Throwable()

// ------------------ RSSI

/**
 * Fired when device disconnect while fetching RSSI
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class RssiDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "RssiDeviceDisconnected() ${super.toString()}"
    }
}

/**
 * Fired when RSSI request cannot be proceeded
 */
class CannotInitializeRssiReading(device: BluetoothDevice) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeRssiReading() ${super.toString()}"
    }
}

/**
 * Fired when RSSI request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class RssiReadingFailed(val reason: Int, val device: BluetoothDevice) : Throwable() {
    override fun toString(): String {
        return "RssiReadingFailed(reason=$reason, device=$device)"
    }
}


// ------------------ Services

/**
 * Fired when device disconnect while discovering services
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class DiscoverServicesDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "DiscoverServicesDeviceDisconnected() ${super.toString()}"
    }
}

/**
 * Fired when service discover request cannot be proceeded
 */
class CannotInitializeServicesDiscovering(device: BluetoothDevice) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeServicesDiscovering() ${super.toString()}"
    }
}

/**
 * Fired when service discovering request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class ServiceDiscoveringFailed(val reason: Int, val device: BluetoothDevice) : Throwable() {
    override fun toString(): String {
        return "ServiceDiscoveringFailed(reason=$reason, device=$device)"
    }
}

/**
 * Fired when trying to find a characteristic whereas services are not discovered
 */
class FindCharacteristicButServiceNotDiscovered(val device: BluetoothDevice, val characteristicUUID: UUID) : Throwable() {
    override fun toString(): String {
        return "FindCharacteristicButServiceNotDiscovered(device=$device, characteristicUUID=$characteristicUUID)"
    }
}

/**
 * Fired when calling [BluetoothGattCharacteristic.characteristic] and the characteristic is not
 * found. Fired only for the method [android.bluetooth.BluetoothGatt.characteristic].
 */
class CharacteristicNotFound(val device: BluetoothDevice, val characteristicUUID: UUID) : Throwable() {
    override fun toString(): String {
        return "CharacteristicNotFound(device=$device, characteristicUUID=$characteristicUUID)"
    }
}


// ------------------ MTU

/**
 * Fired when device disconnect while requesting MTU
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class MtuDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "MtuDeviceDisconnected() ${super.toString()}"
    }
}

/**
 * Fired when MTU request cannot be proceeded
 */
class CannotInitializeMtuRequesting(device: BluetoothDevice) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeMtuRequesting() ${super.toString()}"
    }
}

/**
 * Fired when MTU request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class MtuRequestingFailed(val reason: Int, val device: BluetoothDevice) : Throwable() {
    override fun toString(): String {
        return "MtuRequestingFailed(reason=$reason, device=$device)"
    }
}


// ------------------ Bluetooth exceptions for I/O only

/**
 * Top error corresponding to a device disconnection. Each I/O method like
 * [android.bluetooth.BluetoothGatt.write] have an specific implementation of
 * [DeviceDisconnected], in this case it's [CharacteristicWriteDeviceDisconnected].
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
abstract class DeviceDisconnected(val device: BluetoothDevice, val reason: Int) : Throwable() {
    override fun toString(): String {
        return "DeviceDisconnected(device=$device, reason=$reason)"
    }
}

/**
 * Top error corresponding to an error write preparing I/O. Each I/O method like
 * [android.bluetooth.BluetoothGatt.write] have an specific implementation of
 * [CannotInitialize], in this case it's [CannotInitializeCharacteristicWrite].
 */
abstract class CannotInitialize(val device: BluetoothDevice) : Throwable() {
    override fun toString(): String {
        return "CannotInitialize(device=$device)"
    }
}

/**
 * Exception fired when the current connectLegacy connection is lost
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class GattDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "GattDeviceDisconnected() ${super.toString()}"
    }
}

/**
 * Exception fired when trying to connect on a remote device while local device doesn't support
 * bluetooth
 */
class LocalDeviceDoesNotSupportBluetooth : Throwable()


// ------------------ Characteristics Exceptions

// ------------------ READ

/**
 * Fired when device disconnect while reading characteristic
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class CharacteristicReadDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "CharacteristicReadDeviceDisconnected(service=$service, characteristic=$characteristic) ${super.toString()}"
    }
}

/**
 * Fired when read request cannot be proceeded
 */
class CannotInitializeCharacteristicReading(device: BluetoothDevice,
                                            val service: BluetoothGattService?,
                                            val characteristic: BluetoothGattCharacteristic,
                                            val properties: Int,
                                            val internalService: Any,
                                            val clientIf: Any,
                                            val foundDevice: Any?,
                                            val isDeviceBusy: Any) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeCharacteristicReading(service=$service, characteristic=$characteristic, properties=$properties, internalService=$internalService, clientIf=$clientIf, foundDevice=$foundDevice, isDeviceBusy=$isDeviceBusy) ${super.toString()}"
    }
}

/**
 * Fired when read request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class CharacteristicReadingFailed(val reason: Int, val device: BluetoothDevice, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic) : Throwable() {
    override fun toString(): String {
        return "CharacteristicReadingFailed(reason=$reason, device=$device, service=$service, characteristic=$characteristic)"
    }
}

// ------------------ WRITE

/**
 * Fired when device disconnect while writing characteristic
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class CharacteristicWriteDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic, val value: ByteArray) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "CharacteristicWriteDeviceDisconnected(service=$service, characteristic=$characteristic, value=${Arrays.toString(value)}) ${super.toString()}"
    }
}

/**
 * Fired when write request cannot be proceeded
 */
class CannotInitializeCharacteristicWrite(device: BluetoothDevice,
                                          val service: BluetoothGattService?,
                                          val characteristic: BluetoothGattCharacteristic,
                                          val value: ByteArray,
                                          val properties: Int,
                                          val internalService: Any,
                                          val clientIf: Any,
                                          val foundDevice: Any?,
                                          val isDeviceBusy: Any) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeCharacteristicWrite(service=$service, characteristic=$characteristic, value=${Arrays.toString(value)}, properties=$properties, internalService=$internalService, clientIf=$clientIf, foundDevice=$foundDevice, isDeviceBusy=$isDeviceBusy) ${super.toString()}"
    }
}

/**
 * Fired when write request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class CharacteristicWriteFailed(val reason: Int, val device: BluetoothDevice, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic, val value: ByteArray) : Throwable() {
    override fun toString(): String {
        return "CharacteristicWriteFailed(reason=$reason, device=$device, service=$service, characteristic=$characteristic, value=${Arrays.toString(value)})"
    }
}

// ------------------ CHANGED

/**
 * Fired when device disconnect while listening changes on this [characteristic]
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class CharacteristicChangedDeviceDisconnected(bluetoothDevice: BluetoothDevice, reason: Int, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic) : DeviceDisconnected(bluetoothDevice, reason) {
    override fun toString(): String {
        return "CharacteristicChangedDeviceDisconnected(service=$service, characteristic=$characteristic) ${super.toString()}"
    }
}

/**
 * Fired when notification request cannot be proceeded
 */
class CannotInitializeCharacteristicNotification(device: BluetoothDevice,
                                                 val service: BluetoothGattService,
                                                 val characteristic: BluetoothGattCharacteristic,
                                                 val internalService: Any,
                                                 val clientIf: Any,
                                                 val foundDevice: Any?) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeCharacteristicNotification(service=$service, characteristic=$characteristic, internalService=$internalService, clientIf=$clientIf, foundDevice=$foundDevice) ${super.toString()}"
    }
}


// ------------------ Descriptor Exceptions

// ------------------ READ

/**
 * Fired when device disconnect while reading descriptor
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class DescriptorReadDeviceDisconnected(device: BluetoothDevice, reason: Int, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic, val descriptor: BluetoothGattDescriptor) : DeviceDisconnected(device, reason) {
    override fun toString(): String {
        return "DescriptorReadDeviceDisconnected(service=$service, characteristic=$characteristic, descriptor=$descriptor) ${super.toString()}"
    }
}

/**
 * Fired when read request cannot be proceeded
 */
class CannotInitializeDescriptorReading(device: BluetoothDevice,
                                        val service: BluetoothGattService?,
                                        val characteristic: BluetoothGattCharacteristic?,
                                        val descriptor: BluetoothGattDescriptor,
                                        val internalService: Any,
                                        val clientIf: Any,
                                        val foundDevice: Any?,
                                        val isDeviceBusy: Any) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeDescriptorReading(service=$service, characteristic=$characteristic, descriptor=$descriptor, internalService=$internalService, clientIf=$clientIf, foundDevice=$foundDevice, isDeviceBusy=$isDeviceBusy) ${super.toString()}"
    }
}

/**
 * Fired when read request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class DescriptorReadingFailed(val reason: Int, val device: BluetoothDevice, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic, val descriptor: BluetoothGattDescriptor) : Throwable() {
    override fun toString(): String {
        return "DescriptorReadingFailed(reason=$reason, device=$device, service=$service, characteristic=$characteristic, descriptor=$descriptor)"
    }
}


// ------------------ WRITE

/**
 * Fired when device disconnect while writing descriptor
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class DescriptorWriteDeviceDisconnected(device: BluetoothDevice, reason: Int, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic, val descriptor: BluetoothGattDescriptor, val value: ByteArray) : DeviceDisconnected(device, reason) {
    override fun toString(): String {
        return "DescriptorWriteDeviceDisconnected(service=$service, characteristic=$characteristic, descriptor=$descriptor, value=${Arrays.toString(value)}) ${super.toString()}"
    }
}

/**
 * Fired when write request cannot be proceeded
 */
class CannotInitializeDescriptorWrite(device: BluetoothDevice,
                                      val service: BluetoothGattService?,
                                      val characteristic: BluetoothGattCharacteristic?,
                                      val descriptor: BluetoothGattDescriptor,
                                      val value: ByteArray,
                                      val internalService: Any,
                                      val clientIf: Any,
                                      val foundDevice: Any?,
                                      val isDeviceBusy: Any) : CannotInitialize(device) {
    override fun toString(): String {
        return "CannotInitializeDescriptorWrite(service=$service, characteristic=$characteristic, descriptor=$descriptor, value=${Arrays.toString(value)}, internalService=$internalService, clientIf=$clientIf, foundDevice=$foundDevice, isDeviceBusy=$isDeviceBusy) ${super.toString()}"
    }
}

/**
 * Fired when write request returns an error
 * The [reason] field is filled with an [Int] provided by the Android framework. Android sources are
 * undocumented but [reason] seems to refers to theses consts :
 * [https://android.googlesource.com/platform/external/bluetooth/bluedroid/+/android-5.1.0_r1/stack/include/gatt_api.h]
 */
class DescriptorWriteFailed(val reason: Int, val device: BluetoothDevice, val service: BluetoothGattService, val characteristic: BluetoothGattCharacteristic, val descriptor: BluetoothGattDescriptor, val value: ByteArray) : Throwable() {
    override fun toString(): String {
        return "DescriptorWriteFailed(reason=$reason, device=$device, service=$service, characteristic=$characteristic, descriptor=$descriptor, value=${Arrays.toString(value)})"
    }
}