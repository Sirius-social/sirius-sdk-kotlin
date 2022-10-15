package com.sirius.library.utils.json_canonical

import kotlin.math.ceil

internal object NumberCachedPowers {
    const val kD_1_LOG2_10 = 0.30102999566398114 //  1 / lg(10)
    fun getCachedPower(e: Int, alpha: Int, gamma: Int, c_mk: NumberDiyFp): Int {
        val kQ = NumberDiyFp.kSignificandSize
        val k: Double = ceil((alpha - e + kQ - 1) * kD_1_LOG2_10)
        val index = (GRISU_CACHE_OFFSET + k.toInt() - 1) / CACHED_POWERS_SPACING + 1
        val cachedPower = CACHED_POWERS[index]
        c_mk.setF(cachedPower.significand)
        c_mk.setE(cachedPower.binaryExponent.toInt())
        if(alpha <= c_mk.e() + e && c_mk.e() + e <= gamma){
            return cachedPower.decimalExponent.toInt()
        }
        throw AssertionError()
    }

    // Code below is converted from GRISU_CACHE_NAME(8) in file "powers-ten.h"
    // Regexp to convert this from original C++ source:
    // \{GRISU_UINT64_C\((\w+), (\w+)\), (\-?\d+), (\-?\d+)\}
    // interval between entries  of the powers cache below
    const val CACHED_POWERS_SPACING = 8
    val CACHED_POWERS = arrayOf(
        CachedPower(-0x19e530fcc2e5ba21L, (-1087.toShort()).toShort(), (-308.toShort()).toShort()),
        CachedPower(-0x548f01e838653936L, (-1060.toShort()).toShort(), (-300.toShort()).toShort()),
        CachedPower(-0x884e03414323b1L, (-1034.toShort()).toShort(), (-292.toShort()).toShort()),
        CachedPower(-0x41a96e10be9429f4L, (-1007.toShort()).toShort(), (-284.toShort()).toShort()),
        CachedPower(-0x722fe0526f8003c4L, (-980.toShort()).toShort(), (-276.toShort()).toShort()),
        CachedPower(-0x2caea3d7ceaa657dL, (-954.toShort()).toShort(), (-268.toShort()).toShort()),
        CachedPower(-0x628e53705259364bL, (-927.toShort()).toShort(), (-260.toShort()).toShort()),
        CachedPower(-0x1563dd88dc117435L, (-901.toShort()).toShort(), (-252.toShort()).toShort()),
        CachedPower(-0x5133b66ebf87ac93L, (-874.toShort()).toShort(), (-244.toShort()).toShort()),
        CachedPower(-0x7dc3ed86a24931a9L, (-847.toShort()).toShort(), (-236.toShort()).toShort()),
        CachedPower(-0x3def6bc9b204a9c9L, (-821.toShort()).toShort(), (-228.toShort()).toShort()),
        CachedPower(-0x6f691590c7b767b1L, (-794.toShort()).toShort(), (-220.toShort()).toShort()),
        CachedPower(-0x288b7a34da7dc539L, (-768.toShort()).toShort(), (-212.toShort()).toShort()),
        CachedPower(-0x5f7930326840680cL, (-741.toShort()).toShort(), (-204.toShort()).toShort()),
        CachedPower(-0x10cbf567e8d5531bL, (-715.toShort()).toShort(), (-196.toShort()).toShort()),
        CachedPower(-0x4dc79804d5ca4d72L, (-688.toShort()).toShort(), (-188.toShort()).toShort()),
        CachedPower(-0x7b372b202d39c0c5L, (-661.toShort()).toShort(), (-180.toShort()).toShort()),
        CachedPower(-0x3a22bbd8e52c3246L, (-635.toShort()).toShort(), (-172.toShort()).toShort()),
        CachedPower(-0x6c94603144da366aL, (-608.toShort()).toShort(), (-164.toShort()).toShort()),
        CachedPower(-0x245393db829d5a7cL, (-582.toShort()).toShort(), (-156.toShort()).toShort()),
        CachedPower(-0x5c5499a7f2a0250aL, (-555.toShort()).toShort(), (-148.toShort()).toShort()),
        CachedPower(-0xc1d076c213c0edaL, (-529.toShort()).toShort(), (-140.toShort()).toShort()),
        CachedPower(-0x4a4a525755007f48L, (-502.toShort()).toShort(), (-132.toShort()).toShort()),
        CachedPower(-0x789da0fa9383b575L, (-475.toShort()).toShort(), (-124.toShort()).toShort()),
        CachedPower(-0x3643009fcb3ecfadL, (-449.toShort()).toShort(), (-116.toShort()).toShort()),
        CachedPower(-0x69b17a736e45d9abL, (-422.toShort()).toShort(), (-108.toShort()).toShort()),
        CachedPower(-0x200688db8fd68143L, (-396.toShort()).toShort(), (-100.toShort()).toShort()),
        CachedPower(-0x59204260471a4771L, (-369.toShort()).toShort(), (-92.toShort()).toShort()),
        CachedPower(-0x756a030778b826cL, (-343.toShort()).toShort(), (-84.toShort()).toShort()),
        CachedPower(-0x46bb8f6c70576431L, (-316.toShort()).toShort(), (-76.toShort()).toShort()),
        CachedPower(-0x75f70f0740f0ea95L, (-289.toShort()).toShort(), (-68.toShort()).toShort()),
        CachedPower(-0x324fdaaa9acece4aL, (-263.toShort()).toShort(), (-60.toShort()).toShort()),
        CachedPower(-0x66c01d392f848054L, (-236.toShort()).toShort(), (-52.toShort()).toShort()),
        CachedPower(-0x1ba3ef3bd5d4c4faL, (-210.toShort()).toShort(), (-44.toShort()).toShort()),
        CachedPower(-0x55dbdb66968c6d2dL, (-183.toShort()).toShort(), (-36.toShort()).toShort()),
        CachedPower(-0x2784a0d7cff35f2L, (-157.toShort()).toShort(), (-28.toShort()).toShort()),
        CachedPower(-0x431af79b6deee515L, (-130.toShort()).toShort(), (-20.toShort()).toShort()),
        CachedPower(-0x734333f690af7734L, (-103.toShort()).toShort(), (-12.toShort()).toShort()),
        CachedPower(-0x2e48e8a71de69ad4L, (-77.toShort()).toShort(), (-4.toShort()).toShort()),
        CachedPower(-0x63c0000000000000L, (-50.toShort()).toShort(), 4.toShort()),
        CachedPower(-0x172b5af000000000L, (-24.toShort()).toShort(), 12.toShort()),
        CachedPower(-0x5287143a539e0000L, 3.toShort(), 20.toShort()),
        CachedPower(-0x7ec0c687076bf67cL, 30.toShort(), 28.toShort()),
        CachedPower(-0x3f68318436f8ea4dL, 56.toShort(), 36.toShort()),
        CachedPower(-0x7081cd318415a390L, 83.toShort(), 44.toShort()),
        CachedPower(-0x2a2dc75b54167f98L, 109.toShort(), 52.toShort()),
        CachedPower(-0x60b0d8d9e865ddbbL, 136.toShort(), 60.toShort()),
        CachedPower(-0x129c5dce2b3b04d9L, 162.toShort(), 68.toShort()),
        CachedPower(-0x4f219ac773375258L, 189.toShort(), 76.toShort()),
        CachedPower(-0x7c38f771e5549a25L, 216.toShort(), 84.toShort()),
        CachedPower(-0x3ba2e206bd8ee266L, 242.toShort(), 92.toShort()),
        CachedPower(-0x6db296d359e418a8L, 269.toShort(), 100.toShort()),
        CachedPower(-0x25fe119be58f7216L, 295.toShort(), 108.toShort()),
        CachedPower(-0x5d925c66651088b6L, 322.toShort(), 116.toShort()),
        CachedPower(-0xdf687844b82947bL, 348.toShort(), 124.toShort()),
        CachedPower(-0x4bab1b5e8622e789L, 375.toShort(), 132.toShort()),
        CachedPower(-0x79a4796da4643a3eL, 402.toShort(), 140.toShort()),
        CachedPower(-0x37caac3a3769a2c3L, 428.toShort(), 148.toShort()),
        CachedPower(-0x6ad54ba305685f4dL, 455.toShort(), 156.toShort()),
        CachedPower(-0x21b96042665fa01dL, 481.toShort(), 164.toShort()),
        CachedPower(-0x5a643dcb24c673dbL, 508.toShort(), 172.toShort()),
        CachedPower(-0x939658d5c6760a4L, 534.toShort(), 180.toShort()),
        CachedPower(-0x482340acab164132L, 561.toShort(), 188.toShort()),
        CachedPower(-0x77030ce80dddbe1eL, 588.toShort(), 196.toShort()),
        CachedPower(-0x33df31642ca3875bL, 614.toShort(), 204.toShort()),
        CachedPower(-0x67e9a50c84deac21L, 641.toShort(), 212.toShort()),
        CachedPower(-0x1d5f4a2368e0cfc6L, 667.toShort(), 220.toShort()),
        CachedPower(-0x57262eaca31c4c6aL, 694.toShort(), 228.toShort()),
        CachedPower(-0x46483265b58bbc4L, 720.toShort(), 236.toShort()),
        CachedPower(-0x4489b3b3585bbbf0L, 747.toShort(), 244.toShort()),
        CachedPower(-0x7454711049bf63e6L, 774.toShort(), 252.toShort()),
        CachedPower(-0x2fe010ef59a87bd4L, 800.toShort(), 260.toShort()),
        CachedPower(-0x64ef5b1a166eced7L, 827.toShort(), 268.toShort()),
        CachedPower(-0x18ef64045e63f363L, 853.toShort(), 276.toShort()),
        CachedPower(-0x53d7df269dc40bd7L, 880.toShort(), 284.toShort()),
        CachedPower(-0x7fbbb4a18558307bL, 907.toShort(), 292.toShort()),
        CachedPower(-0x40de1bbffc5322d3L, 933.toShort(), 300.toShort()),
        CachedPower(-0x719863d0a1bb0071L, 960.toShort(), 308.toShort()),
        CachedPower(-0x2bcce862637347bfL, 986.toShort(), 316.toShort()),
        CachedPower(-0x61e6246d4b1ce457L, 1013.toShort(), 324.toShort()),
        CachedPower(-0x1469409145208827L, 1039.toShort(), 332.toShort()),
        CachedPower(-0x5078fdc4640f1195L, 1066.toShort(), 340.toShort())
    )
    const val GRISU_CACHE_MAX_DISTANCE = 27

    // nb elements (8): 82
    const val GRISU_CACHE_OFFSET = 308

    internal class CachedPower(
        var significand: Long,
        var binaryExponent: Short,
        var decimalExponent: Short
    )
}
