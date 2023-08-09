<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;
use App\Models\Deposits;

class Members extends Model
{
    use HasFactory;
    protected $table = 'members';

    

    // Relationship with deposit history
    public function depositHistory()
    {
        return $this->hasMany(Deposits::class, 'member_id','member_id');
    }

    // Function to identify active members
    public function isActiveMember()
    {
        $sixMonthsAgo = Carbon::now()->subMonths(6);

        // Check if the user has deposits in the last 6 months
        return $this->depositHistory()->whereDate('date', '>=', $sixMonthsAgo)->exists();
    }

}
