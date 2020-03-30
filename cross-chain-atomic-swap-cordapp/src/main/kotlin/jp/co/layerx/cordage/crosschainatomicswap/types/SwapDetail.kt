package jp.co.layerx.cordage.crosschainatomicswap.types

import net.corda.core.serialization.CordaSerializable
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8

@CordaSerializable
class SwapDetail(val fromEthereumAddress: Address,
                 val toEthereumAddress: Address,
                 val weiAmount: Uint256,
                 val securityAmount: Uint256,
                 val status: ProposalStatus) {
    companion object {
        fun listToSwapDetail(list: List<Any>): SwapDetail {
            if (list.size != 5) {
                throw IllegalArgumentException("An argument must be list which size is 5.")
            }
            return SwapDetail(
                fromEthereumAddress = list[0] as Address,
                toEthereumAddress = list[1] as Address,
                weiAmount = list[2] as Uint256,
                securityAmount = list[3] as Uint256,
                status = ProposalStatus.fromInt(list[4] as Uint8)
            )
        }
    }
}
