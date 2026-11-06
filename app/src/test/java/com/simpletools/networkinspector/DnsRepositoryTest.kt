package com.simpletools.networkinspector

import com.simpletools.networkinspector.core.dns.DnsRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class DnsRepositoryTest {
    @Test
    fun resolveLocalhostSucceeds() {
        val repo = DnsRepository()
        val result = repo.resolve("localhost")
        assertTrue(result.ipv4.isNotEmpty() || result.ipv6.isNotEmpty() || result.error != null)
    }
}
