SUMMARY = "EdgeAI TIOVX Apps"
DESCRIPTION = "EdgeAI TIOVX Apps implements Open VX based simple deep learning demos that runs on TI platforms"
HOMEPAGE = "https://github.com/TexasInstruments/edgeai-tiovx-apps"

LICENSE = "TI-TFL"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=dc68ab0305d85e56491b9a9aed2309f2"

PV = "${SRCPV}"
BRANCH = "main"
SRC_URI = "git://github.com/TexasInstruments/edgeai-tiovx-apps.git;branch=${BRANCH};protocol=https \
 file://0001-edgeai_camera-Fix-Werror-format-security-errors-with.patch"
SRCREV = "5396580a44cb2757a20ad7e57f870f7a4b9d8874"

PLAT_SOC = ""
PLAT_SOC:j721e = "j721e"
PLAT_SOC:ti-j72xx = "j721e"
PLAT_SOC:j721s2 = "j721s2"
PLAT_SOC:j784s4 = "j784s4"
PLAT_SOC:ti-j78xx = "j784s4"
PLAT_SOC:am62axx = "am62a"

S = "${WORKDIR}/git"

DEPENDS = "edgeai-tiovx-modules yaml-cpp glib-2.0"
RDEPENDS:${PN}-source = "cmake bash python3-core"

COMPATIBLE_MACHINE = "j721e-evm|j721e-hs-evm|j721s2-evm|j721s2-hs-evm|j784s4-evm|j784s4-hs-evm|am62axx-evm|ti-j7"

export SOC = "${PLAT_SOC}"

EXTRA_OECMAKE = "-DTARGET_FS=${WORKDIR}/recipe-sysroot -DCMAKE_SKIP_RPATH=TRUE -DCMAKE_OUTPUT_DIR=${WORKDIR}/out"

PACKAGES += "${PN}-source"
FILES:${PN}-source += "/opt"
ALLOW_EMPTY:${PN} = "1"
ALLOW_EMPTY:${PN}-dev = "1"

inherit cmake pkgconfig

do_install() {
    CP_ARGS="-Prf --preserve=mode,timestamps --no-preserve=ownership"

    mkdir -p ${D}/opt/edgeai-tiovx-apps
    cp ${CP_ARGS} ${WORKDIR}/git/* ${D}/opt/edgeai-tiovx-apps
    cp ${CP_ARGS} ${WORKDIR}/out/bin ${D}/opt/edgeai-tiovx-apps
}

PR:append = "_edgeai_2"
