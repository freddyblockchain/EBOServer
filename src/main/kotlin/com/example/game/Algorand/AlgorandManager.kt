package com.example.game.Algorand
import EBOAlgorandClient
import Player
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.example.game.Abilities.AbilityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class AlgorandManager {
    companion object{
        val secretPath = "/run/secrets/server_mnemonic"
        val accountMnemonic = if (File(secretPath).exists()) {
            File(secretPath).readText().trim()
        } else {
            //for running it locally
            System.getenv("SERVER_MMEMONIC") ?: throw IllegalStateException("serverAccountNotFound")
        }
        val serverAccount = Account(accountMnemonic)
        val goldAsa = 676111222L
        val fireballAsa = 676532256L
        val icicleAsa = 677924248L
        val snowballAsa = 677926089L
        val dashAsa = 678786539L
        val abilityAsas = listOf(fireballAsa, icicleAsa, snowballAsa, dashAsa)

        fun handleNewPlayer(newAddress: String){
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            if(!playerIsOptedIntoGold(newAddress)){
                sendMoneyToCoverOptIn(newAddress)
                /*coroutineScope.launch {
                    // wait for client to opt into gold
                    delay(10000)
                    println("sending gold to new player. Have waited for player to receive money and opt into gold.")
                    sendGold(newAddress)
                }*/
            }
        }
        private fun sendMoneyToCoverOptIn(receivingAddress: String){
            val suggestedParams: Response<TransactionParametersResponse> = EBOAlgorandClient.TransactionParams().execute()
            val amount = 650000 // 0. Algo
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

        fun sendGold(receivingAddress: String, amount: Int){
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                val suggestedParams: Response<TransactionParametersResponse> = EBOAlgorandClient.TransactionParams().execute()
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
            }
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

        fun updatePlayerAbilities(player: Player, address: String){
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                val account = EBOAlgorandClient.AccountInformation(Address(address)).execute().body()
                val EBOAssets = account.assets.filter { it.assetId in abilityAsas }.map { it.assetId }

                EBOAssets.forEach {
                    if(it !in player.assets){
                        player.assets.add(it)
                        val ability = AbilityManager.abilityCreatorMap[it]!!()
                        player.abilities.add(ability)
                    }
                }
            }
        }
    }
}