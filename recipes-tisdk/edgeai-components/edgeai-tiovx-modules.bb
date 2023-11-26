SUMMARY = "EdgeAI TIOVX modules"
DESCRIPTION = "EdgeAI TIOVX modules implements simple APIs to create and use OpenVX nodes"
HOMEPAGE = "https://git.ti.com/cgit/edgeai/edgeai-tiovx-modules"

LICENSE = "TI-TFL"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1f7721ee7d288457c5a70d0c8ff44b87"

PV = "${SRCPV}"
BRANCH = "main"
SRC_URI = "git://git.ti.com/git/edgeai/edgeai-tiovx-modules.git;branch=${BRANCH};protocol=https"
SRCREV = "0a2565a83ec378216b1c9047d0f84669bf568de9"

PLAT_SOC = ""
PLAT_SOC:j721e = "j721e"
PLAT_SOC:ti-j72xx = "j721e"
PLAT_SOC:j721s2 = "j721s2"
PLAT_SOC:j784s4 = "j784s4"
PLAT_SOC:ti-j78xx = "j784s4"
PLAT_SOC:am62axx = "am62a"

S = "${WORKDIR}/git"

DEPENDS = "ti-vision-apps edgeai-tiovx-kernels"
RDEPENDS:${PN}-source = "cmake"

COMPATIBLE_MACHINE = "j721e-evm|j721e-hs-evm|j721s2-evm|j721s2-hs-evm|j784s4-evm|j784s4-hs-evm|am62axx-evm|ti-j7"

export SOC = "${PLAT_SOC}"

EXTRA_OECMAKE = "-DTARGET_FS=${WORKDIR}/recipe-sysroot -DINSTALL_SRC=on -DCMAKE_SKIP_RPATH=TRUE -DCMAKE_OUTPUT_DIR=${WORKDIR}/out"

PACKAGES += "${PN}-source"
FILES:${PN}-source += "/opt/"

FILES:${PN} += "/usr/lib \
		/usr/lib64/* \
"
do_install:append:ti-j7 () {

    if [ ${libdir} = "/usr/lib64" ]; then
        mkdir -p ${D}/usr/lib64/
        mv ${D}/usr/lib/libedgeai-tiovx-modules* ${D}/usr/lib64/
    fi
}

inherit cmake

PR:append = "_edgeai_0"
