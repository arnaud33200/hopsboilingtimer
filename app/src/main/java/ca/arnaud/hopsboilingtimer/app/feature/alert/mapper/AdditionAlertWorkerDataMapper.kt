package ca.arnaud.hopsboilingtimer.app.feature.alert.mapper

import androidx.work.Data
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.domain.common.TwoWayMapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject
import androidx.work.Data as WorkerData

class AdditionAlertWorkerDataMapper @Inject constructor() :
    TwoWayMapper<AdditionAlertData, WorkerData> {

    companion object {
        private const val WORKER_DATA_KEY = "alert"
    }

    // Generated by GPT
    // TODO - put try catch?
    override fun mapTo(input: AdditionAlertData): WorkerData {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(input)
        objectOutputStream.close()

        return WorkerData.Builder().putByteArray(
            WORKER_DATA_KEY, byteArrayOutputStream.toByteArray()
        ).build()
    }

    // Generated by GPT
    // TODO - put try catch?
    override fun mapFrom(output: Data): AdditionAlertData {
        val byteArray = output.getByteArray(WORKER_DATA_KEY)
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val alert = objectInputStream.readObject() as AdditionAlertData
        objectInputStream.close()
        return alert
    }
}