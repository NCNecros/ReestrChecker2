package company

import com.linuxense.javadbf.DBFException
import com.linuxense.javadbf.DBFReader
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*
import javax.annotation.Resource


@org.springframework.stereotype.Service
class DBFHelper {
    @Resource
    private var dataFiller: DataFiller = DataFiller()
    private var dbfReader: DBFReader? = null

    constructor() {
    }

    constructor(dataFiller: DataFiller) {
        this.dataFiller = dataFiller
    }

    @Throws(FileNotFoundException::class, DBFException::class)
    fun readFromP(filename: String/*, Map<String, Human> humanMap*/) {
        dataFiller.clearData()
        var row: Array<Any>?
        val dbfReader = DBFReader(FileInputStream(filename)) as DBFReader
        val fieldList = getFieldListFromDBFReader(dbfReader)
        dbfReader.charactersetName = "cp866"

        while (true) {
            row = dbfReader.nextRecord()
            if (row != null) {
                dataFiller.addHumanAndVisit(row, fieldList)
            } else {
                break
            }
        }
    }

    @Throws(FileNotFoundException::class, DBFException::class)
    fun readFromU(filename: String/*, Map<String, Human> humanMap*/) {
        var row: Array<Any>?
        val dbfReader = DBFReader(FileInputStream(filename)) as DBFReader
        dbfReader.charactersetName = "cp866"
        val fieldList = getFieldListFromDBFReader(dbfReader)

        while (true) {
            row = dbfReader.nextRecord()
            if (row != null) {
                dataFiller.addService(row, fieldList)
            } else {
                break
            }
        }
    }

    @Throws(DBFException::class)
    private fun getFieldListFromDBFReader(dbfReader: DBFReader): Map<String, Int> {
        val fieldList = HashMap<String, Int>()
        for (i in 0..dbfReader.fieldCount - 1) {
            val field = dbfReader.getField(i)
            fieldList.put(field.name, i)
        }
        return fieldList
    }

    companion object {

        private val logger = LoggerFactory.getLogger(DBFHelper::class.java)
    }

}