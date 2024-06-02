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

class AlgorandManager {
    companion object{
        val accountMmenonic = System.getenv("SERVER_MMEMONIC") ?: throw IllegalStateException("serverAccountNotFound")
        val serverAccount = Account(accountMmenonic)

        fun handleNewPlayer(newAddress: String){
            if(!playerIsOptedIntoGold(newAddress)){
                sendMoneyToCoverOptIn(newAddress)
            }
        }
        private fun sendMoneyToCoverOptIn(receivingAddress: String){
            val suggestedParams: Response<TransactionParametersResponse> = EBOAlgorandClient.TransactionParams().execute()
            val amount = 1000000 // 1 Algo
            val ptxn: Transaction = Transaction.PaymentTransactionBuilder()
                .sender(serverAccount.address)
                .amount(amount)
                .receiver(receivingAddress)
                .suggestedParams(suggestedParams.body()).build()


            val sptxn: SignedTransaction = serverAccount.signTransaction(ptxn)

            val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(sptxn)
            val resp = EBOAlgorandClient.RawTransaction().rawtxn(encodedTxBytes).execute()

            val txid: String = resp.body().txId
            val result: PendingTransactionResponse = Utils.waitForConfirmation(EBOAlgorandClient, txid, 4)
            println("successfully sent transaction")
        }

        private fun playerIsOptedIntoGold(playerAddress: String): Boolean{
            val goldAsa = 676111222L
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