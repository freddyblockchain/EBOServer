import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.crypto.Signature
import com.example.models.VerificationData

class VerificationManager {
    companion object {
        fun verifyMessage(verificationData: VerificationData): Boolean {
            try {
                val address = Address(verificationData.address)
                val message = verificationData.message.toByteArray()
                val signatureBytes = verificationData.signature
                val verification = address.verifyBytes(message, Signature(signatureBytes))
                return verification
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
    }
}