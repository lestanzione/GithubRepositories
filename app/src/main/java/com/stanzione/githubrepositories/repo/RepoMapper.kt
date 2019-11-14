package com.stanzione.githubrepositories.repo

import com.stanzione.githubrepositories.model.Repo
import com.stanzione.githubrepositories.model.domain.RepoDomain

open class RepoMapper {

    open fun transform(repo: Repo): RepoDomain {
        return RepoDomain(
            id = repo.id,
            name = repo.name,
            forks = repo.forks.toString(),
            stars = repo.stars.toString(),
            ownerName = repo.owner.name,
            avatarUrl = repo.owner.avatarUrl
        )
    }

    open fun transform(repoList: List<Repo>): List<RepoDomain> {
        val repoDomainList = mutableListOf<RepoDomain>()
        repoList.forEach { repo ->
            repoDomainList.add(transform(repo))
        }
        return repoDomainList.toList()
    }

}