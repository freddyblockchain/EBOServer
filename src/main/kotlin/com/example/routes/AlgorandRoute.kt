import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.lang3.ArrayUtils

const val ALGOD_API_ADDR = "https://testnet-algorand.api.purestake.io/ps2"
const val ALGOD_PORT = 443
const val ALGOD_API_TOKEN = ""
val token = "GCAE61OEML9V1hZKxCYf6EJBOGMHwvd15zmeM4Li"
var headers = arrayOf("X-API-Key")
val values = arrayOf(token)
lateinit var algodClient: AlgodClient




fun Route.algorand() {

    val sender = Account("digital dragon soap ride draw top increase peasant balcony announce scrub bachelor truth language awkward daring typical news income lyrics pen hood wise able element")

    route("/algo") {
        post {
            algodClient = AlgodClient(ALGOD_API_ADDR,ALGOD_PORT,ALGOD_API_TOKEN)
            println("about to send transaction")
            firstTransaction(sender)
            call.respondText("Successfull")
        }
    }
}

@Throws(java.lang.Exception::class)
private fun printBalance(myAccount: Account): String? {
    val myAddress = myAccount.address.toString()
    println("about to send transaction 2")
    val respAcct: Response<com.algorand.algosdk.v2.client.model.Account> = algodClient.AccountInformation(myAccount.address).execute(headers,values)
    println("about to send transaction 3")
    if (!respAcct.isSuccessful()) {
        throw java.lang.Exception(respAcct.message())
    }
    val accountInfo: com.algorand.algosdk.v2.client.model.Account = respAcct.body()
    println(String.format("Account Balance: %d microAlgos", accountInfo.amount))
    return myAddress
}

@Throws(Exception::class)
fun firstTransaction(myAccount: Account) {
    printBalance(myAccount)
    try {
        // Construct the transaction
        val RECEIVER = "HZ57J3K46JIJXILONBBZOHX6BKPXEM2VVXNRFSUED6DKFD5ZD24PMJ3MVA"
        val note = "Hello World"
        val resp: Response<TransactionParametersResponse> = algodClient.TransactionParams().execute(headers,values)
        if (!resp.isSuccessful) {
            throw Exception(resp.message())
        }
        val params: TransactionParametersResponse = resp.body() ?: throw Exception("Params retrieval error")
        println("Algorand suggested parameters: ${params}")
        val txn: Transaction = Transaction.PaymentTransactionBuilder()
            .sender(myAccount.address.toString())
            .note(note.toByteArray())
            .amount(1000) // 1 algo = 1000000 microalgos
            .receiver(Address(RECEIVER))
            .suggestedParams(params)
            .build()

        // Sign the transaction
        val signedTxn: SignedTransaction = myAccount.signTransaction(txn)
        println("Signed transaction with txid: " + signedTxn.transactionID)

        // Submit the transaction to the network
        val txHeaders: Array<String> = ArrayUtils.add(headers, "Content-Type")
        val txValues: Array<String> = ArrayUtils.add(values, "application/x-binary")
        // Submit the transaction to the network
        val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(signedTxn)
        val rawtxresponse: Response<PostTransactionsResponse> =
            algodClient.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues)
        if (!rawtxresponse.isSuccessful) {
            throw Exception(rawtxresponse.message())
        }
        val id: String = rawtxresponse.body().txId

        // Wait for transaction confirmation
        waitForConfirmation(algodClient,id, headers,values)
        printBalance(myAccount)
    } catch (e: Exception) {
        System.err.println("Exception when calling algod#transactionInformation: " + e.message)
    }
}

@Throws(java.lang.Exception::class)
fun waitForConfirmation(client: AlgodClient, txID: String?, headers: Array<String>, values: Array<String>) {
    var lastRound = client.GetStatus().execute(headers, values).body().lastRound
    while (true) {
        try {
            // Check the pending tranactions
            val pendingInfo = client.PendingTransactionInformation(txID).execute(headers, values).body()
            if (pendingInfo.confirmedRound != null && pendingInfo.confirmedRound > 0) {
                println("Transaction confirmed in round " + pendingInfo.confirmedRound)
                break
            }
            lastRound = lastRound!! + 1
            client.WaitForBlock(lastRound).execute(headers, values)
        } catch (e: java.lang.Exception) {
            throw e
        }
    }
}
