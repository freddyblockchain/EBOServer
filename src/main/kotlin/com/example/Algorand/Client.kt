
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.IndexerClient

val algod = AlgodClient("https://testnet-api.algonode.cloud", 443, "")
val indexer = IndexerClient("https://testnet-idx.algonode.cloud", 443, "")

val dummyAccount = Account("" +
        "glance fame avocado team tobacco spoon actress author situate swarm embark check design reform radio alien bachelor matter best diesel whip select idle absorb film")
val AanAppId: Long = 614314262