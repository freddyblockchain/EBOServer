package com.example.Algorand
import EBOAlgorandClient
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.Utils
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlgorandManager {
    companion object{
        val accountMmenonic = System.getenv("SERVER_MMEMONIC") ?: throw IllegalStateException("serverAccountNotFound")
        val serverAccount = Account(accountMmenonic)
        val goldAsa = 676111222L

        fun handleNewPlayer(newAddress: String){
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            if(!playerIsOptedIntoGold(newAddress)){
                sendMoneyToCoverOptIn(newAddress)
                coroutineScope.launch {
                    // wait for client to opt into gold
                    delay(10000)
                    println("sending gold to new player. Have waited for player to receive money and opt into gold.")
                    sendStartingGold(newAddress)
                }
            }
        }
        private fun sendMoneyToCoverOptIn(receivingAddress: String){
            val suggestedParams: Response<TransactionParametersResponse> = EBOAlgorandClient.TransactionParams().execute()
            val amount = 600000 // 0.6 Algo
            val ptxn: Transaction = Transaction.PaymentTransactionBuilder()
                .sender(serverAccount.address)
                .amount(amount)
                .receiver(receivingAddress)
                .suggestedParams(suggestedParams.body()).build()


            val sptxn: SignedTransaction = serverAccount.signTransaction(ptxn)

            val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(sptxn)
            val resp = EBOAlgorandClient.RawTransaction().rawtxn(encodedTxBytes).execute()
            println("Sent transaction to new player")
        }

        private fun sendStartingGold(receivingAddress: String){
            val suggestedParams: Response<TransactionParametersResponse> = EBOAlgorandClient.TransactionParams().execute()
            val amount = 1 // 1 gold
            val ptxn: Transaction = Transaction.AssetTransferTransactionBuilder()
                .sender(serverAccount.address)
                .assetAmount(amount)
                .assetIndex(goldAsa)
                .assetReceiver(receivingAddress)
                .suggestedParams(suggestedParams.body())
                .build()

            val sptxn: SignedTransaction = serverAccount.signTransaction(ptxn)

            val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(sptxn)
            val resp = EBOAlgorandClient.RawTransaction().rawtxn(encodedTxBytes).execute()

            println("sent gold to new player")
        }

        private fun playerIsOptedIntoGold(playerAddress: String): Boolean{
            try {
                // Fetch account information
                val accountInfo = EBOAlgorandClient.AccountInformation(Address(playerAddress)).execute().body()
                // Check asset holdings
                for (asset in accountInfo.assets) {
                    if (asset.assetId == goldAsa) {
                        return true
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }
}